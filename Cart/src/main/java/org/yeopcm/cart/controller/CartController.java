package org.yeopcm.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    @GetMapping("/")
    public String Hello() {
        return "Cart 페이지 구성완료.";
    }
}
