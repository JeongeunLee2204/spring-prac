package com.example.shop.sales;

import com.example.shop.Item;
import com.example.shop.member.CustomUser;
import com.example.shop.member.Member;
import com.example.shop.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesController {
    private final SalesRepository salesRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/order")
    String postOrder(@RequestParam String title,
                     @RequestParam Integer price,
                     @RequestParam Integer count,
                     Authentication auth)
    {
        Sales sales=new Sales();

        sales.setItemName(title);
        sales.setPrice(price);
        sales.setCount(count);
        CustomUser user=(CustomUser) auth.getPrincipal();
        System.out.println(user.id);
        var member=new Member();
        member.setId(user.id);
        sales.setMember(member);
        salesRepository.save(sales);

        return "redirect:/list";

    }

    @GetMapping("/order/all")
    String getOrderAll(){
        List<Sales> result=salesRepository.customFindAll();
        System.out.println(result);
        var salesDto=new SalesDto();
        salesDto.itemName=result.get(0).getItemName();
        salesDto.price=result.get(0).getPrice();
        salesDto.username=result.get(0).getMember().getUsername();

        var aaa=memberRepository.findById(1L);
        System.out.println("--------------------------------------");
        System.out.println(aaa);
        return "list.html";
    }


    class SalesDto {
        public String itemName;
        public Integer price;
        public String username;
    }
}
