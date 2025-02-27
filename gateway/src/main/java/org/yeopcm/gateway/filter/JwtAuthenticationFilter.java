package org.yeopcm.gateway.filter;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.yeopcm.gateway.util.JwtTokenProvider;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ReactiveValueOperations<String, String> reactiveValueOperations;

    public static class Config {}

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = new RedisTemplate<>();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String path = request.getURI().getPath();
            if (path.startsWith("/user/") && !path.startsWith("/user/info") && !path.startsWith("/user/logout")) {
                return chain.filter(exchange);
            }

            String token = jwtTokenProvider.resolveToken(request);
            System.out.println("token: " + token);

            return reactiveValueOperations.get(token)
                    .defaultIfEmpty("continue")  // null 대신 "continue" 방출
                    .flatMap(value -> {
                        // 블랙리스트에 등록된 토큰이면 UNAUTHORIZED 반환
                        if (!"continue".equals(value)) {
                            System.out.println("블랙리스트 도착함");
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                        // 블랙리스트에 없다면 다음 로직 진행
                        return Mono.just("proceed");
                    })
                    .flatMap(ignored -> {
                        System.out.println("블랙리스트 아님");
                        if (token != null && jwtTokenProvider.validateToken(token)) {
                            Authentication authentication = jwtTokenProvider.getAuthentication(token);
                            return chain.filter(exchange)
                                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                        }
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

}
