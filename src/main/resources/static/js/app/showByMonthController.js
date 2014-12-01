'use strict';

bozorApp.controller('ShowMonthController', [ '$scope', 'productSvc', '$modal', '$timeout', 'dialogsSvc',
    '$routeParams', 'selectorPresenter', 'notification',
    function ($scope, productSvc, $modal, $timeout, dialogsSvc, $routeParams, selectorPresenter, notification) {
        init();

        $scope.dayName = function (dayStr) {
            var mmt = moment(dayStr, _shortDateFormat);

            return mmt.format("D");
        };

        $scope.totalPrice = function (item, notFormated) {
            var total = 0;

            angular.forEach(item.items, function (s) {
                total += calcExpression(s.price);
            });

            return notFormated ? total : formatPrice(total);
        };

        $scope.calcPrice = function(item){
            var price = calcExpressionWithError(item.price);

            if (price){
                return formatPrice(price);
            }

            return  $('#_invalidexp').val();
        };

        function calcAvgDayPrice(total, count) {
            return count > 0 ? total / count : 0;
        }

        function calcPrices() {
            var total = 0;

            angular.forEach($scope.items, function (s) {
                total += $scope.totalPrice(s, true);
            });

            $scope.monthPrice = formatPrice(total);
            $scope.avgDayPrice = formatPrice(calcAvgDayPrice(total, $scope.items.length));
        };

        function init() {
            document.title = $('#_titlemonth').val();
            moment.locale(getCurrentLocale());
            selectorPresenter.stop();

            var month = moment($routeParams.month, _shortMonthFormat);
            if (!month.isValid()) {
                month = moment();
                $routeParams.month = month.format(_shortMonthFormat);
            }

            $scope.monthName = getMonthName(month);
            $scope.commands = getMonthCommands(month, 'm');

            var prevMonth = month.clone().subtract(1, 'month').format(_shortMonthFormat);
            var nextMonth = month.clone().add(1, 'month').format(_shortMonthFormat);

            $('#prevDay').attr('href', '#m' + prevMonth);
            $('#nextDay').attr('href', '#m' + nextMonth);

            productSvc.listMonthItems({month: $routeParams.month}, function (data) {
                $scope.items = data;
                calcPrices();
            }, function () {
                notification.error('Load items failed')
            });
        }
    }]);
