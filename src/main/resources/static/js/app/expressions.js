'use strict';

var _REGEXP_EXPRESSION_VALID = new RegExp('^[\\d\\.\\+\\-\\*\\/\\(\\)\\s]+$');
var _expressionCache = new Object();

function calcExpression(exp) {
    var val = calcExpressionWithError(exp);
    return val != null ? val : 0;
}

function isExpressionValid(exp) {
    return calcExpressionWithError(exp) != null;
}

function calcExpressionWithError(exp0) {
    if (exp0 == null || exp0 === ''){
        return 0;
    }

    if (_expressionCache[exp0] !== undefined){
        return _expressionCache[exp0];
    }

    var exp = (exp0 || '').replace(/,/g, '.');
    var result = null;

    if (_REGEXP_EXPRESSION_VALID.test(exp)) {
        try {
            var val = eval(exp);

            if (val !== Infinity) {
                result = val;
            }
        }
        catch (e) {
        }
    }

    _expressionCache[exp0] = result;

    return result;
}
