package com.example.shop.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    String register(Authentication auth){
        if (auth.isAuthenticated()){
            return "redirect:/list";
        }
        return "register.html";
    }
    @PostMapping("/member")
    String addMember(String username, String password, String displayName){
        Member member= new Member();
        member.setUsername(username);
        //new BCryptPasswordEncoder().encode(password);
        var hash=passwordEncoder.encode(password);
        member.setPassword(hash);
        member.setDisplayName(displayName);
        memberRepository.save(member);
        return "redirect:/list";
    }
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }
    @GetMapping("/mypage")
    public String myPage(Authentication auth){
        System.out.println(auth);
        System.out.println(auth.getName());
        System.out.println(auth.isAuthenticated());
        System.out.println(auth.getPrincipal());

        com.example.shop.member.CustomUser result=(com.example.shop.member.CustomUser) auth.getPrincipal();
        System.out.println(result.displayName);
        return "mypage.html";
    }

    @GetMapping("/user/1")
    @ResponseBody
    public MemberDto getUser(){
        var a=memberRepository.findById(1L);
        var result=a.get();
        var data=new MemberDto(result.getUsername(),result.getDisplayName());
        //data.username=result.getUsername();
        //data.displayname=result.getDisplayName();
        return data;
    }

    @GetMapping("/v2/user/1")
    @ResponseBody
    public MemberDto getUser2(){
        var a=memberRepository.findById(1L);
        var result=a.get();
        var data=new MemberDto(result.getUsername(),result.getDisplayName());
        data.id=1L;
        return data;
    }
}

@Getter
class MemberDto{
    public String username;
    public String displayname;
    public Long id;
    MemberDto(String a, String b){
        this.username=a;
        this.displayname=b;
    }
    MemberDto(String a, String b, Long id){
        this.username=a;
        this.displayname=b;
        this.id=id;
    }
}
