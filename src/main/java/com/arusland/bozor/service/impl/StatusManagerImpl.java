package com.arusland.bozor.service.impl;

import com.arusland.bozor.service.StatusManager;
import com.arusland.bozor.dto.StatusResult;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ruslan on 02.08.2014.
 */
@Service
public class StatusManagerImpl implements StatusManager {
    private static Pattern TOKEN_PATTERN = Pattern.compile("^[A-Fa-f\\d]{4}$");
    private static Object lock = new Object();
    private static byte itemsCounter = 1;
    private static byte productsCounter = 1;

    public StatusResult hasUpdates(String token) {
        Matcher match = TOKEN_PATTERN.matcher(token);

        if (match.find()) {
            byte[] parts = Hex.decode(token);
            byte itemsC = parts[0];
            byte productsC = parts[1];

            return new StatusResult(itemsC != itemsCounter, productsC != productsCounter, getToken());
        }

        return new StatusResult(getToken());
    }

    public void modifyItems() {
        synchronized (lock) {
            itemsCounter++;
        }
    }

    public void modifyProducts() {
        synchronized (lock) {
            productsCounter++;
        }
    }

    private String getToken() {
        return String.format("%02x%02x", itemsCounter, productsCounter);
    }
}
