package com.arusland.bozor.util;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by ruslan on 12.10.2014.
 */
public class JsUtils {
    private final static Pattern VALIDATION = Pattern.compile("^[\\d\\.\\+\\-\\*/\\(\\)\\s]+$");
    private final static HashMap<String, Double> cache = new HashMap<>();

    public static Double eval(String expression0) {
        if (StringUtils.isNotBlank(expression0)) {

            synchronized (cache) {
                if (cache.containsKey(expression0)) {
                    return cache.get(expression0);
                }
            }

            String expression = expression0.replaceAll(",", ".");

            if (VALIDATION.matcher(expression).matches()) {
                try {
                    Context context = Context.enter();
                    Scriptable scope = context.initStandardObjects();

                    Object val = context.evaluateString(scope, expression, "sourceName", 1, null);

                    if (val != null) {
                        Double result;

                        if (val instanceof Double) {
                            result = (Double) val;
                        } else {
                            result = Double.valueOf(val.toString());
                        }

                        synchronized (cache) {
                            cache.put(expression0, result);
                        }

                        return result;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    Context.exit();
                }
            }
        }

        return new Double(0);
    }
}
