Array.prototype.insert = function (index, item) {
    this.splice(index, 0, item);
};

Array.prototype.remove = function (item) {
    var index = this.indexOf(item);

    if (index > -1) {
        this.splice(index, 1);
    }
};

String.prototype.startsWith = function (str){
    return this.indexOf(str) == 0;
};

var stringIsBlank = function(str){
  return str == null || str == '';
};

var stringIsNotBlank = function(str){
    return !stringIsBlank(str);
};

var stringGetShort = function(str, length){
    if (stringIsNotBlank(str) && str.length > length){
        str = str.substring(0, length) + "...";
    }

    return str;
};

var arrayClone = function(arr){
    var result = JSON.parse(JSON.stringify(arr));

    return result;
};

var _longDateFormat = 'YYYYMMDDHHmmss';
var _shortDateFormat = 'YYYYMMDD';
var _shortMonthFormat = 'YYYYMM';

function strGetWhatelse(){
    return $('#_whatelse').val();
};

var formatShortDate = function(dt){
    return moment(dt).format(_shortDateFormat);
};

var isToday = function(dt){
    var todayStr = moment().format(_shortDateFormat);
    var dayStr = dt.format(_shortDateFormat);

    return dayStr === todayStr;
};

var dayComment = function(dt){
    var now = moment();
    var prevDay = now.clone().subtract(1, 'day').format(_shortDateFormat);
    var nextDay = now.clone().add(1, 'day').format(_shortDateFormat);
    var dayStr = dt.format(_shortDateFormat);

    var result = '';

    if (isToday(dt)){
        result = $('#_today').val();
    } else if (dayStr === prevDay)    {
        result = $('#_yesterday').val();
    } else if (dayStr === nextDay){
        result = $('#_tomorrow').val();
    }

    return result;
};

var arrayGetChanges = function (newArray, oldArray) {
    var result = [];

    for (var i = 0; i < newArray.length; i++) {
        var objNew = newArray[i];

        for (var j = 0; j < oldArray.length; j++) {
            var objOld = oldArray[j]

            if (objNew.id == objOld.id) {
                if (objNew.price != objOld.price
                    || objNew.comment != objOld.comment
                    || objNew.amount != objOld.amount) {
                    result.push(objNew);
                }

                break;
            }
        }
    }

    return result;
};

var productsAreDifferent = function (newArray, oldArray) {
    if (!newArray || !oldArray){
        return true;
    }

    if (newArray.length != oldArray.length){
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

        if (!found){
            return true;
        }
    }

    return false;
};

var parsePrice = function (price) {
    var result = parseFloat(price);

    return isNaN(result) ? 0 : result;
};
