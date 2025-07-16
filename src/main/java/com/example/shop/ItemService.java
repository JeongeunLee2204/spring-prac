package com.example.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(String title, Integer price){
        if(price < 0){
            throw new RuntimeException("음수 안됨");
        }
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        itemRepository.save(item);
    }
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }
}