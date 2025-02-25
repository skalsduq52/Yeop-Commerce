package org.yeopcm.user.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yeopcm.user.dto.UserRequestDTO;
import org.yeopcm.user.dto.UserResponseDTO;
import org.yeopcm.user.exception.DuplicateEmailException;
import org.yeopcm.user.repository.UserRepository;
import org.yeopcm.user.util.JwtTokenProvider;
import org.yeopcm.user.vo.User;

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

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        redisTemplate.opsForValue().set("RefreshToken:" + user.getEmail(), refreshToken, 7, TimeUnit.DAYS);

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .accessToken(accessToken)
                .build();
    }

    public UserResponseDTO getUser(String email) {

        User user = userRepository.findByEmail(email);

        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }
}
