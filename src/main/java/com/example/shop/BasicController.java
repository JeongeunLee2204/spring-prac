package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Controller
public class BasicController {
    @GetMapping("/")
    @ResponseBody
    String mainpage(){
        return "메인페이지";
    }

    @GetMapping("/경로")
    @ResponseBody
    String hello(){
        return "이거보세요";
    }

    @GetMapping("/마이페이지")
    @ResponseBody
    String mypage(){return "마이페이지입니다~";}

    @GetMapping("/html연습")
    String htmlprac(){return "index.html";}

    LocalDateTime now=LocalDateTime.now();
    @GetMapping("/date")
    @ResponseBody
    String date(){return now.toString();}
}
