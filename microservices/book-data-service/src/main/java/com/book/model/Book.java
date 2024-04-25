package com.book.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Book {

    Long id;
    String name;
    String ISBN;
    Long currentStock;

    public void updateStock(Long newStock){
        this.currentStock = newStock;
    }
}
