package com.arusland.bozor.domain;



import java.util.List;

/**
 * Created by ruslan on 12.09.2014.
 */
public class StatusMonth {
    private String day;
    private List<ProductItemShort> items;

    public StatusMonth(String day, List<ProductItemShort> items) {
        this.day = day;
        this.items = items;
    }

    public String getDay() {
        return day;
    }

    public List<ProductItemShort> getItems() {
        return items;
    }
}
