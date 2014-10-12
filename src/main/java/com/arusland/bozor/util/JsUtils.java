package com.arusland.bozor.util;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Created by ruslan on 12.10.2014.
 */
public class JsUtils {
    public static Double eval(String expression) {
        Double result = new Double(0);

        if (StringUtils.isNotBlank(expression)) {
            try {
                Context context = Context.enter();
                Scriptable scope = context.initStandardObjects();

                Object val = context.evaluateString(scope, expression, "sourceName", 1, null);

                if (val != null) {
                    if (val instanceof Double) {
                        result = (Double) val;
                    } else {
                        result = Double.valueOf(val.toString());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                Context.exit();
            }
        }

        return result;
    }
}
