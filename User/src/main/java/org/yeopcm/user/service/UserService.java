package org.yeopcm.user.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yeopcm.user.dto.LoginRequestDTO;
import org.yeopcm.user.dto.UserRequestDTO;
import org.yeopcm.user.dto.UserResponseDTO;
import org.yeopcm.user.exception.DuplicateEmailException;
import org.yeopcm.user.exception.LoginValidException;
import org.yeopcm.user.repository.UserRepository;
import org.yeopcm.user.util.JwtTokenProvider;
import org.yeopcm.user.vo.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        if(userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .phone(userDTO.getPhone())
                .name(userDTO.getName())
                .address(userDTO.getAddress())
                .build();
        userRepository.save(user);

        String accessToken = generateToken(user.getEmail());

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .build();
    }

    public UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new LoginValidException("아이디 혹은 비밀번호를 확인해주세요"));

        if(!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new LoginValidException("아이디 혹은 비밀번호를 확인해주세요");
        }

        String accessToken = generateToken(user.getEmail());

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .build();
    }

    public String logoutUser(String token) {
        String jwtToken = token.substring(7);

        redisTemplate.opsForValue().set(jwtToken,"logout",1, TimeUnit.HOURS);// Authorities (권한 정보)

        String claim = jwtTokenProvider.getClaimsFromToken(jwtToken).getSubject();
        SecurityContextHolder.clearContext();

        redisTemplate.delete(claim);

        return "로그아웃 성공";
    }

    public UserResponseDTO getUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 업습니다."));

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }

    public String generateToken(String email) {
        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        redisTemplate.opsForValue().set(email, refreshToken, 7, TimeUnit.DAYS);

        return accessToken;
    }
}
