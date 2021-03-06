'use strict';

bozorApp.controller('ShowDayController', ['$scope', 'productSvc', '$modal', '$timeout', 'dialogsSvc',
    '$routeParams', 'selectorPresenter', "notification",
    function ($scope, productSvc, $modal, $timeout, dialogsSvc, $routeParams, selectorPresenter, notification) {
        var LOCAL_TIMEOUT = 3 * 1000; // in milliseconds
        init();

        function init() {
            $scope.modified = false;
            $scope.lastUpdate = null;
            $scope.oldItems = [];
            moment.locale(getCurrentLocale());
            document.title = $('#_titleshow').val();
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

            $scope.day = day;
        }

        function onNewItems(newItems) {
            handleUpdateLocalItems(function () {
                if (!$scope.items) {
                    $scope.items = [];
                }

                if (mergeItems(newItems, $scope.items)) {
                    $scope.oldItems = arrayClone(newItems);
                }
            });
        }

        function handleUpdateLocalItems(handler) {
            if (!$scope.lastUpdate) {
                handler();
                return true;
            }

            var ms = moment().diff($scope.lastUpdate);

            if (ms >= LOCAL_TIMEOUT) {
                handler();
                return true;
            }

            console.log('skip updating local items');

            $timeout(function () {
                handleUpdateLocalItems(function () {
                    selectorPresenter.repeatQuery();
                });
            }, 1000);

            return false;
        }

        function onNewItem(newItem) {
            $scope.items.push(newItem);
        }

        function preAddingNewItem(newItem) {
            if (!$scope.isToday) {
                newItem.date = $scope.day.format(_longDateFormat);
                newItem.bought = true;
            }
        }

        function applyWrapper(handler) {
            $scope.$apply(handler);
        }

        function handleError(error) {
            if (error)
                notification.error(error);
        }

        function canCheckStatus() {
            return !dialogsSvc.isShown();
        }

        selectorPresenter.init('selector-place', onNewItems, onNewItem, applyWrapper,
            handleError, canCheckStatus, $routeParams.time, preAddingNewItem);

        $scope.$watch(
            "items",
            function (newValue, oldValue) {
                if (newValue && oldValue) {
                    $scope.modified = true;
                    $scope.lastUpdate = moment();
                }
            },
            true
        );

        $scope.saveChanges = function () {
            if ($scope.modified) {
                $scope.modified = false;

                var newItems = arrayGetChanges($scope.items, $scope.oldItems);

                if (newItems.length > 0) {
                    console.log('items modified count: ' + newItems.length);

                    angular.forEach(newItems, function (item) {
                        if (item.price) {
                            item.bought = true;
                        }
                    });

                    $scope.oldItems = arrayClone($scope.items);

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
                notification.error('Save failed!');
            });
        }

        $scope.total = function () {
            var total = 0;

            angular.forEach($scope.items, function (s) {
                total += calcExpression(s.price);
            });

            return formatPrice(total);
        };

        $scope.calcPrice = function (item) {
            var price = calcExpressionWithError(item.price);

            if (price) {
                return formatPrice(price);
            }

            return $('#_invalidexp').val();
        };

        $scope.validatePrice = function (item) {
            return isExpressionValid(item.price) ? '' : 'has-error';
        };

        $scope.editItem = function (item) {
            dialogsSvc.showItemEditDlg(item);
        };

        $scope.removeItem = function (item) {
            $scope.items.remove(item);
            productSvc.removeProductItem({itemKey: item.id}, function () {
            }, function () {
                notification.error('Save failed');
            });
        };

        $scope.buyItem = function (item) {
            productSvc.buyProductItem({key: item.id}, function (itemSaved) {
                    item.date = itemSaved.date;
                },
                function () {
                    notification.error('Save failed');
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

        $scope.getItemClass = function (item) {
            return item.price == null || item.price == '' ? 'danger' : '';
        }
    }]);
