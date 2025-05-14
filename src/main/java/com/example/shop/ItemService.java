package com.example.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public void saveItem(String title, Integer price){
        //itemRepository.save(item);
        Item item=new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
    }
}
