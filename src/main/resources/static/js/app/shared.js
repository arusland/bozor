Array.prototype.insert = function (index, item) {
    this.splice(index, 0, item);
};

Array.prototype.remove = function (item) {
    var index = this.indexOf(item);

    if (index > -1) {
        this.splice(index, 1);
    }
};

String.prototype.startsWith = function (str) {
    return this.indexOf(str) == 0;
};

String.prototype.format = function () {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

String.prototype.zerofy = function (length) {
    var lhs = this;

    while (lhs.length < length) {
        lhs = "0" + lhs;
    }

    return lhs;
};

String.prototype.capitalize = function () {

    if (this.length > 1) {
        return this.substring(0, 1).toUpperCase() + this.substring(1);
    }

    if (this.length == 1) {
        this.toUpperCase();
    }

    return this;
};

function zerofy(value, length) {
    return value.toString().zerofy(length);
};

var stringIsBlank = function (str) {
    return str == null || str == '';
};

var stringIsNotBlank = function (str) {
    return !stringIsBlank(str);
};

var stringGetShort = function (str, length) {
    if (stringIsNotBlank(str) && str.length > length) {
        str = str.substring(0, length) + "...";
    }

    return str;
};

var arrayClone = function (arr) {
    var result = JSON.parse(JSON.stringify(arr));

    return result;
};

var _longDateFormat = 'YYYYMMDDHHmmss';
var _shortDateFormat = 'YYYYMMDD';
var _shortMonthFormat = 'YYYYMM';

function strGetWhatelse() {
    return $('#_whatelse').val();
};

var formatShortDate = function (dt) {
    return moment(dt).format(_shortDateFormat);
};

var formatShortMonth = function (dt) {
    return moment(dt).format(_shortMonthFormat);
};

var isToday = function (dt) {
    var todayStr = moment().format(_shortDateFormat);
    var dayStr = dt.format(_shortDateFormat);

    return dayStr === todayStr;
};

var dayComment = function (dt) {
    var now = moment();
    var prevDay = now.clone().subtract(1, 'day').format(_shortDateFormat);
    var nextDay = now.clone().add(1, 'day').format(_shortDateFormat);
    var dayStr = dt.format(_shortDateFormat);

    var result = '';

    if (isToday(dt)) {
        result = $('#_today').val();
    } else if (dayStr === prevDay) {
        result = $('#_yesterday').val();
    } else if (dayStr === nextDay) {
        result = $('#_tomorrow').val();
    }

    return result;
};

var arrayGetChanges = function (newArray, oldArray) {
    var result = [];

    for (var i = 0; i < newArray.length; i++) {
        var objNew = newArray[i];
        var found = false;

        for (var j = 0; j < oldArray.length; j++) {
            var objOld = oldArray[j]

            if (objNew.id == objOld.id) {
                if (objNew.price != objOld.price
                    || objNew.comment != objOld.comment
                    || objNew.date !== objOld.date
                    || objNew.amount != objOld.amount) {
                    result.push(objNew);
                }

                found = true;
                break;
            }
        }

        if (!found){
            result.push(objNew);
        }
    }

    return result;
};

var productsAreDifferent = function (newArray, oldArray) {
    if (!newArray || !oldArray) {
        return true;
    }

    if (newArray.length != oldArray.length) {
        return true;
    }

    for (var i = 0; i < newArray.length; i++) {
        var objNew = newArray[i];
        var found = false;

        for (var j = 0; j < oldArray.length; j++) {
            var objOld = oldArray[j]

            if (objNew.id == objOld.id) {
                if (objNew.name != objOld.name
                    || objNew.type != objOld.type) {
                    return true;
                }

                found = true;

                break;
            }
        }

        if (!found) {
            return true;
        }
    }

    return false;
};

var itemsAreDifferent = function (newArray, oldArray) {
    if (!newArray || !oldArray) {
        return true;
    }

    if (newArray.length != oldArray.length) {
        return true;
    }

    for (var i = 0; i < newArray.length; i++) {
        var objNew = newArray[i];
        var found = false;

        for (var j = 0; j < oldArray.length; j++) {
            var objOld = oldArray[j]

            if (objNew.id === objOld.id) {
                if (objNew.price !== objOld.price
                    || objNew.date !== objOld.date
                    || objNew.amount !== objOld.amount
                    || objNew.comment !== objOld.comment
                    || objNew.productId !== objOld.productId) {
                    return true;
                }

                found = true;
                break;
            }
        }

        if (!found) {
            return true;
        }
    }

    return false;
};

var mergeItems = function (newArray, oldArray) {
    if (!newArray || !oldArray) {
        if (oldArray){
            oldArray.length = 0;
        }

        return true;
    }

    var itemsToAdd = [];
    var itemsFound = [];
    var modified = false;

    for (var i = 0; i < newArray.length; i++) {
        var objNew = newArray[i];
        var found = false;

        for (var j = 0; j < oldArray.length; j++) {
            var objOld = oldArray[j]

            if (objNew.id === objOld.id) {
                itemsFound.push(objOld);

                // update item if it changed
                if (objNew.price !== objOld.price
                    || objNew.date !== objOld.date
                    || objNew.amount !== objOld.amount
                    || objNew.comment !== objOld.comment
                    || objNew.productId !== objOld.productId) {

                    objOld.price = objNew.price;
                    objOld.date = objNew.date;
                    objOld.amount = objNew.amount;
                    objOld.comment = objNew.comment;
                    objOld.productId = objNew.productId;
                    modified = true;
                }

                found = true;
                break;
            }
        }

        if (!found) {
            itemsToAdd.push(objNew);
        }
    }

    var itemsToRemove = [];

    for (var j = 0; j < oldArray.length; j++) {
        var objOld = oldArray[j];

        if (itemsFound.indexOf(objOld) < 0){
            itemsToRemove.push(objOld);
        }
    }

    for (var j = 0; j < itemsToRemove.length; j++) {
        var objOld = itemsToRemove[j];

        var index = oldArray.indexOf(objOld);

        if (index >= 0){
            oldArray.splice(index, 1);
            modified = true;
        }
    }

    for (var j = 0; j < itemsToAdd.length; j++) {
        oldArray.push(itemsToAdd[j]);
        modified = true;
    }

    return modified;
};

function getMonthName(mmt, index, skipYear) {
    var year = mmt.format("YYYY");

    if (typeof index === "number") {
        mmt = moment(year + zerofy(index, 2) + "01", _shortDateFormat);
    }

    if (skipYear !== true) {
        return mmt.format("MMMM").capitalize() + " (" + year + ")";
    }

    return mmt.format("MMMM").capitalize();
};

function getMonthCommands(day, linkPrefix) {
    day = day || moment();
    var commands = [];
    var year = day.format("YYYY");

    for (var i = 1; i <= 12; i++) {
        commands.push({name: getMonthName(day, i, true), link: '#' + linkPrefix + year + zerofy(i, 2)});
    }

    return commands;
};

function getCurrentLocale() {
    return $('#_locale').val();
};

function formatPrice(val) {
    try {
        var result = val.toString();

        if (result.indexOf('.') > 0) {
            return val.toFixed(2);
        }

        return result;
    }
    catch (e) {
        return val;
    }
}
