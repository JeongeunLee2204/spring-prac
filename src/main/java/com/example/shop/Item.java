package com.example.shop;

import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 200)
    public String title;

    public Integer price;

    public Item() {}

    public Item(String title, Integer price) {
        this.title = title;
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
