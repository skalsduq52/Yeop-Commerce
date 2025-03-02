package org.yeopcm.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.yeopcm.user.dto.LoginRequestDTO;
import org.yeopcm.user.dto.UserRequestDTO;
import org.yeopcm.user.dto.UserResponseDTO;
import org.yeopcm.user.repository.UserRepository;
import org.yeopcm.user.service.UserService;
import org.yeopcm.user.vo.User;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getUserPage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User 페이지 구성완료.");
        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원가입
     * @param userRequestDTO email(ID), 비밀번호, 이름, 전화번호, 주소
     * @return 201 + Location + Access 토큰
     */
    @PostMapping("/join")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(userRequestDTO.getEmail())
                .toUri();

        return ResponseEntity.created(location)
                .header("Authorization","Bearer "+ userResponseDTO.getAccessToken())
                .body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

        UserResponseDTO userResponseDTO = userService.loginUser(loginRequestDTO);

        return ResponseEntity.accepted().header("Authorization","Bearer "+ userResponseDTO.getAccessToken())
                .body(userResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String token){

        String response = userService.logoutUser(token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponseDTO> getUserInfo(@RequestHeader("Authorization") String token){

        UserResponseDTO responseDTO = userService.getUser(token);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO){

        UserResponseDTO responseDTO = userService.updateUser(userRequestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/validatePassword")
    public ResponseEntity<UserResponseDTO> validatePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> requestBody){

        UserResponseDTO responseDTO = userService.validatePassword(token, requestBody.get("password"));

        return ResponseEntity.ok(responseDTO);
    }
}
