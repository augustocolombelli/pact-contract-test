package com.book.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Book {

    Long id;
    String name;
    String ISBN;
    Long stock;

    public void updateStock(Long newStock){
        this.stock = newStock;
    }
}
