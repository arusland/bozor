package com.arusland.bozor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ruslan on 24.10.2014.
 */
@Controller
public class ResourceController {
    private final static Pattern PROPVAR = Pattern.compile("\\.([a-z])([a-z]*)",
            Pattern.CASE_INSENSITIVE);

    @ResponseBody
    @RequestMapping(value = "/res/locales.js",
            produces = "application/javascript; charset=utf-8")
    public String getStatus(Locale locale, HttpServletResponse response) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages/messages", locale);

        StringBuffer result = new StringBuffer();
        result.append("// Auto-generated variables from java locale file\n\n");
        result.append("$currentLocale='");
        result.append(locale.getLanguage());
        result.append("';\n");

        for (String key : bundle.keySet()) {
            makeJsVariable(result, key, bundle.getString(key));
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        return result.toString();
    }

    private static void makeJsVariable(StringBuffer builder, String key, String value) {
        Matcher m = PROPVAR.matcher(key);

        builder.append("$");

        while (m.find()) {
            m.appendReplacement(builder,
                    m.group(1).toUpperCase() + m.group(2));
        }

        builder.append("='");
        builder.append(value);
        builder.append("';\n");
    }
}
