package com.example.baseball_back.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Test 컨트롤러", description = "test 관련 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class TestController {


    @GetMapping("/test")
    public ResponseEntity<String> test() throws Exception {

       String test = "테스트 성공";
        return ResponseEntity.ok(test);
    }

    @GetMapping("/")
    public ResponseEntity hello() {
        return ResponseEntity.ok("자동 배포 자동화 테스트");
    }

}
