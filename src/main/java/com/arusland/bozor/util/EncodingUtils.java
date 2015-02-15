package com.arusland.bozor.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by ruslan on 16.02.2015.
 */
public class EncodingUtils {

    public static String normalizeValue(String value) {
        try {
            String result = new String(value.getBytes("ISO-8859-1"), "UTF-8");

            return result;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
