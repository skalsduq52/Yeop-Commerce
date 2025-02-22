package org.yeopcm.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String Hello() {
        return "ㅋㅋ 내가 이겼다 이 자식들아";
    }
}
