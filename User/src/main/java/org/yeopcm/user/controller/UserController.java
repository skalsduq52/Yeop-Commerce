package org.yeopcm.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String Hello() {
        return "Jenkins CI/CD 파이프라인 구축 테스트 하겠습니다.";
    }
}
