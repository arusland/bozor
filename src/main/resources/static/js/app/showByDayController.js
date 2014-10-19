'use strict';

bozorApp.controller('ShowDayController', [ '$scope', 'productSvc', '$modal', '$timeout', 'dialogsSvc', '$routeParams', 'selectorPresenter',
    function ($scope, productSvc, $modal, $timeout, dialogsSvc, $routeParams, selectorPresenter) {
        $scope.modified = false;
        moment.locale(getCurrentLocale());

        var day = moment($routeParams.time, _shortDateFormat);

        if (!day.isValid()) {
            day = moment();
            $routeParams.time = day.format(_shortDateFormat);
        }

        $scope.today = day.format('dddd, MMMM Do YYYY').capitalize();
        $scope.dayComment = dayComment(day);
        $scope.isToday = isToday(day);
        $scope.commands = getMonthCommands(day, 'm');

        var prevDay = day.clone().subtract(1, 'day').format(_shortDateFormat);
        var nextDay = day.clone().add(1, 'day').format(_shortDateFormat);

        $('#prevDay').attr('href', '#d' + prevDay);
        $('#nextDay').attr('href', '#d' + nextDay);

        function onNewItems(newItems) {
            if (itemsAreDifferent(newItems, $scope.oldItems)) {
                $scope.items = newItems;
                $scope.oldItems = arrayClone(newItems);
            }
        }

        function onNewItem(newItem) {
            if (!$scope.isToday) {
                newItem.date = day.format(_longDateFormat);
                newItem.bought = true;
            }

            $scope.items.push(newItem);
        }

        function applyWrapper(handler) {
            $scope.$apply(handler);
        }

        function handleError(error) {
            if (error)
                alert(error);
        }

        function canCheckStatus() {
            return !dialogsSvc.isShown();
        }

        selectorPresenter.init('selector-place', onNewItems, onNewItem, applyWrapper,
            handleError, canCheckStatus, $routeParams.time);

        $scope.$watch(
            "items",
            function (newValue, oldValue) {
                if (newValue && oldValue) {
                    $scope.modified = true;

                    //console.log('Items modified');
                }
            },
            true
        );

        $scope.saveChanges = function () {
            if ($scope.modified) {
                $scope.modified = false;
                var newItems = arrayGetChanges($scope.items, $scope.oldItems);
                $scope.oldItems = arrayClone($scope.items);

                if (newItems.length > 0) {
                    console.log('items modified count: ' + newItems.length);

                    var counter = 0;
                    saveProductItem(newItems[0], function () {
                        return ++counter < newItems.count ? newItems[counter] : null;
                    });

                    return;
                }
            }

            $timeout($scope.saveChanges, 1000);
        };

        $timeout($scope.saveChanges, 1000);

        var saveProductItem = function (item, getNextItem) {
            productSvc.saveProductItem(item, function () {
                item = getNextItem();

                if (item) {
                    saveProductItem(item, getNextItem);
                } else {
                    $timeout($scope.saveChanges, 1000);
                }
            }, function () {
                alert('Save failed!');
            });
        }

        $scope.total = function () {
            var total = 0;

            angular.forEach($scope.items, function (s) {
                total += calcExpression(s.price);
            });

            return total;
        };

        $scope.calcPrice = function(item){
            return calcExpressionWithError(item.price) || $('#_invalidexp').val();
        };

        $scope.validatePrice = function(item){
            return isExpressionValid(item.price) ? '' : 'has-error';
        };

        $scope.editItem = function (item) {
            dialogsSvc.showItemEditDlg(item);
        };

        $scope.removeItem = function (item) {
            $scope.items.remove(item);
            productSvc.removeProductItem({itemKey: item.id}, function () {
            }, function () {
                alert('Save item failed')
            });
        };

        $scope.buyItem = function (item) {
            productSvc.buyProductItem({key: item.id}, function (itemSaved) {
                    item.date = itemSaved.date;
                },
                function () {
                    alert('Save item failed')
                });

            item.bought = true;
        };

        $scope.itemComment = function (item) {
            var result = item.amount || '';

            if (item.comment) {
                if (result) {
                    result += ' ';
                }

                result += item.comment;
            }

            return result;
        };
    }]);
