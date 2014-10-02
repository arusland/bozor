package com.arusland.bozor.dto;

/**
 * Created by ruslan on 02.10.2014.
 */
public class StatusResult {
    public final boolean hasNewItems;
    public final boolean hasNewProducts;
    public final boolean hasUpdates;
    public final String token;

    public StatusResult(boolean hasNewItems, boolean hasNewProducts, String token) {
        this.hasNewItems = hasNewItems;
        this.hasNewProducts = hasNewProducts;
        this.token = token;
        hasUpdates = hasNewItems || hasNewProducts;
    }

    public StatusResult(String token) {
        hasNewItems = true;
        hasNewProducts = true;
        hasUpdates = true;
        this.token = token;
    }
}
