package com.example.demo.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 프로젝트 전체 REST API의 예외를 감시
public class GlobalExceptionHandler {

    // Service나 Controller에서 IllegalArgumentException이 발생하면 이 메소드가 가로챔
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        // AdminApiController의 try-catch에서 만들어주던 형태와 동일하게 400 에러와 메시지 반환
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}