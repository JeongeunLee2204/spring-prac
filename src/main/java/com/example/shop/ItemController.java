package com.example.shop;

import com.example.shop.comment.Comment;
import com.example.shop.comment.CommentRepository;
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
    private final CommentRepository commentRepository;

    @PostConstruct
    public void init() {
        try {
            itemRepository.deleteAll();
            if (itemRepository.count() == 0) {
                for(int i=0;i<2;i++){
                itemRepository.save(new Item("바지", 10000));
                itemRepository.save(new Item("셔츠", 15000));
                itemRepository.save(new Item("신발끈", 30000));
                itemRepository.save(new Item("코끼리", 10000));
                itemRepository.save(new Item("거북이", 15000));
                itemRepository.save(new Item("고양이", 30000));
                itemRepository.save(new Item("acc", 10000));
                itemRepository.save(new Item("aaa", 15000));
                itemRepository.save(new Item("abc", 30000));
            }}
        } catch (Exception e) {
            System.err.println("init() 예외 발생:");
            e.printStackTrace(); // 실제 에러 콘솔에 출력
        }
    }


    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);
        model.addAttribute("totalPages", 1);     // ✅ 페이지 수 고정
        model.addAttribute("currentPage", 1);    // ✅ 현재 페이지 번호 추가
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
    List<Comment> comments=commentRepository.findAllByParentId(id);
    model.addAttribute("comments", comments);
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

        model.addAttribute("items", result.getContent());
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

    @GetMapping("/search")
    public String postSearch(@RequestParam String searchText, Model model){
        var result = itemRepository.findAllByTitleContains(searchText);
        //var result = itemRepository.rawQuery1(searchText);
        model.addAttribute("items", result);
        model.addAttribute("totalPages", 1);  // 👈 반드시 추가
        model.addAttribute("currentPage", 1); // 👈 반드시 추가
        return "list.html";
    }


}

