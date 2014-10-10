'use strict';

bozorApp.controller('ShowMonthController', [ '$scope', 'productSvc', '$modal', '$timeout', 'dialogsSvc', '$routeParams', 'selectorPresenter',
    function ($scope, productSvc, $modal, $timeout, dialogsSvc, $routeParams, selectorPresenter) {
        $scope.modified = false;
        moment.locale('ru');

        var month = moment($routeParams.month, _shortMonthFormat);

        if (!month.isValid()) {
            month = moment();
            $routeParams.month = month.format(_shortMonthFormat);
        }

        $scope.monthName = getMonthName(month);
        $scope.commands = getMonthCommands(month);

        var prevMonth = month.clone().subtract(1, 'month').format(_shortMonthFormat);
        var nextMonth = month.clone().add(1, 'month').format(_shortMonthFormat);

        $('#prevDay').attr('href', '#m' + prevMonth);
        $('#nextDay').attr('href', '#m' + nextMonth);

        productSvc.listMonthItems({month: $routeParams.month}, function (data) {
            $scope.items = data;
        }, function () {
            alert('Load items failed')
        });

        $scope.totalPrice = function(item){
            var total = 0;

            angular.forEach(item.items, function (s) {
                total += calcExpression(s.price);
            });

            return total;
        }

        $scope.dayName = function (dayStr) {
            var mmt = moment(dayStr, _shortDateFormat);

            return mmt.format("D");
        }
    }]);
