package com.arusland.bozor.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ruslan on 19.07.2014.
 */
public class ServletUtils {

    public static String getAction(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String path = request.getServletPath();

        int index = uri.indexOf(path);

        if (index == 0) {
            return uri.substring(path.length());
        }

        throw new IllegalStateException();
    }

    public static List<String> getActions(HttpServletRequest request) {
        List<String> result = new LinkedList<String>();

        for (String str : getAction(request).split("/")) {
            int index = str.indexOf(";jsessionid");

            if (index > -1){
                str = str.substring(0, index);
            }

            if (StringUtils.isNotBlank(str)) {
                result.add(str);
            }
        }

        return result;
    }
}
