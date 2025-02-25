package org.yeopcm.user.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.yeopcm.user.dto.UserRequestDTO;
import org.yeopcm.user.dto.UserResponseDTO;
import org.yeopcm.user.repository.UserRepository;
import org.yeopcm.user.service.UserService;
import org.yeopcm.user.vo.User;

import java.net.URI;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String Hello() {
        return "User 페이지 구성완료.";
    }

    /**
     * 회원가입
     * @param userRequestDTO email(ID), 비밀번호, 이름, 전화번호, 주소
     * @return 201 + Location + Access 토큰
     */
    @PostMapping("/")
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

    @GetMapping("/info")
    public ResponseEntity<UserResponseDTO> getUserInfo(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        UserResponseDTO responseDTO = userService.getUser(email);

        return ResponseEntity.ok(responseDTO);
    }
}
