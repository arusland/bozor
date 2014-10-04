package com.arusland.bozor.dto;

import com.arusland.bozor.domain.Product;
import com.arusland.bozor.dto.ProductItemDto;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ruslan on 01.08.2014.
 */
public class Status {
    private final static String PRODUCTS = "products";
    private final static String ITEMS = "items";

    private final String token;
    private final HashMap<String, Object> updates;

    public Status(String token) {
        this.token = token;
        updates = new HashMap<String, Object>();
    }

    public Status(String token, List<ProductItemDto> items, List<Product> products) {
        this(token);

        if (items != null) {
            updates.put(ITEMS, items);
        }

        if (products != null) {
            updates.put(PRODUCTS, products);
        }
    }

    public String getToken() {
        return token;
    }

    public HashMap<String, Object> getUpdates() {
        return updates;
    }
}
