package com.example.shop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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

    @PostConstruct
    public void init() {
        itemRepository.deleteAll();
        if (itemRepository.count() == 0) {
            itemRepository.deleteAll();
            itemRepository.save(new Item("바지", 10000));
            itemRepository.save(new Item("셔츠", 15000));
            itemRepository.save(new Item("신발끈", 30000));
        }
    }


    @GetMapping("/list")
    String list(Model model){
        List<Item> result = itemRepository.findAll();

        for (Item item : result) {
            System.out.println("title = " + item.title);
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
        Optional<Item> result = itemRepository.findById(id);
        //var result=itemRepository.findById(1L);
        if (result.isPresent()) {
            System.out.println(result.get());
            model.addAttribute("data", result.get());
            return "detail.html";
        }
        else{
            return "redirect:/list";
        }
    }


    @PostMapping("/add")
    String addPost(String title, Integer price) {
        itemService.saveItem(title,price);
        return "redirect:/list";
    }
}
