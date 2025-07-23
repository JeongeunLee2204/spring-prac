package com.example.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
    @Service
    public class MyUserDetailsService implements UserDetailsService {
        private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var result = memberRepository.findByUsername(username);
        if (result.isEmpty()){
            throw new UsernameNotFoundException("그런 아이디 없음");
        }
        var user = result.get();
        List<GrantedAuthority>권한=new ArrayList<>();
        권한.add(new SimpleGrantedAuthority("일반유저"));
        var a=new com.example.shop.member.CustomUser(user.getUsername(),user.getPassword(),권한);
        a.displayName=user.getDisplayName();
        a.id=user.getId();
        return a;
    }

}

