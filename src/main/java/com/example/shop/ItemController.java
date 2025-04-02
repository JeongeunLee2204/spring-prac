package com.example.shop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("바지", 10000));
        itemRepository.save(new Item("셔츠", 15000));
        itemRepository.save(new Item("신발끈", 30000));
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
}
