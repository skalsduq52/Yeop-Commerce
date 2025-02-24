package org.yeopcm.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @GetMapping("/")
    public String Hello() {
        return "Search 페이지 구성완료.";
    }
}
