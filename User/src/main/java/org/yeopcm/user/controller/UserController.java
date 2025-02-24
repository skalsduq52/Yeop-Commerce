package org.yeopcm.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String Hello() {
        return "User 페이지 구성완료.";
    }
}
