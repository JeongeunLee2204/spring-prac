package com.example.shop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final ItemSendService itemSendService;
    private final S3Service s3Service;

    @PostConstruct
    public void init() {
        try {
            itemRepository.deleteAll();
            if (itemRepository.count() == 0) {
                itemRepository.save(new Item("바지", 10000));
                itemRepository.save(new Item("셔츠", 15000));
                itemRepository.save(new Item("신발끈", 30000));
            }
        } catch (Exception e) {
            System.err.println("init() 예외 발생:");
            e.printStackTrace(); // 실제 에러 콘솔에 출력
        }
    }


    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll();

        for (Item item : result) {
            System.out.println("title = " + item.getTitle());
        }

        model.addAttribute("items", result);
        return "list.html";
    }

    @GetMapping("/write")
    String write(Model model){
        return "write.html";
    }
//    @PostMapping("/add")
//    String addPost(@RequestParam String title, @RequestParam Integer price) {
//        Item item=new Ite`m();
//        item.setTitle(title);
//        item.setPrice(price);
//        itemRepository.save(item);
//        return "redirect:/list";
//    }
@GetMapping("/detail/{id}")
String detail(@PathVariable Long id, Model model) {
    if (itemSendService.sendItem(id, model)) {
        return "detail.html";
    } else {
        return "redirect:/list";
    }
}



    @PostMapping("/add")
    String addPost(String title, Integer price) {
        itemService.saveItem(title,price);
        return "redirect:/list";
    }


    @PostMapping("/test1")
    String test1(@RequestBody Map<String,Object> body){
        System.out.println(body.get("name"));
        return "redirect:/list";
    }

    @DeleteMapping("/item")
    ResponseEntity<String> deleteItem(@RequestParam Long id){
        itemRepository.deleteById(id);
        return ResponseEntity.status(200).body("삭제완료");
    }

    @PostMapping("/test2")
    String test2(){
        var result=new BCryptPasswordEncoder().encode("문자");
        System.out.println(result);
        return "redirect:/list";
    }
    @GetMapping("/list/page/{abc}")
    String getListPage(Model model, @PathVariable Integer abc){
        Page<Item> result= itemRepository.findPageBy(PageRequest.of(abc-1,5));

        model.addAttribute("items", result);
        model.addAttribute("totalPages",result.getTotalPages());
        return "list.html";
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename){
        System.out.println(filename);
        var result = s3Service.createPresignedUrl("test/" + filename); // ⬅ 수정
        return result;
    }

}

