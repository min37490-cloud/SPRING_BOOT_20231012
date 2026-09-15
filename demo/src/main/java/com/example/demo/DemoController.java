package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 컨트롤러 어노테이션 명시 
public class DemoController {
    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 반갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }


@GetMapping("/hello2") 
    public String hello2(Model model) {
        // 5개의 속성 추가
        model.addAttribute("name", "이민형");
        model.addAttribute("studentId", "20231012");
        model.addAttribute("department", "미디어소프트웨어");
        model.addAttribute("week", "2주차");
        model.addAttribute("message", "스프링 부트 실습 과제 완료!");
        return "hello2"; // hello2.html 연결
    }
}