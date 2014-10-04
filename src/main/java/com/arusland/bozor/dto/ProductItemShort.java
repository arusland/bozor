package com.arusland.bozor.dto;

import com.arusland.bozor.domain.ProductItem;

/**
 * Created by ruslan on 11.09.2014.
 */
public class ProductItemShort {
    private String name;
    private Double price;

    public ProductItemShort(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public static ProductItemShort from(ProductItem item) {
        return new ProductItemShort(item.getProduct().getName(), item.getPrice());
    }
}
