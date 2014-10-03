'use strict';

bozorApp.controller('MainController', [ '$scope', 'productSvc', '$modal', '$timeout', 'dialogsSvc', 'selectorPresenter',
    function ($scope, productSvc, $modal, $timeout, dialogsSvc, selectorPresenter) {
        $scope.items = [];

        function onNewItems(newItems){
            $scope.items = newItems;
        }

        function onNewItem(newItem) {
            $scope.items.push(newItem);
        }

        function applyWrapper(handler) {
            $scope.$apply(handler);
        }

        function canCheckStatus() {
            return !dialogsSvc.isShown();
        }

        $(document).ready(function () {
              selectorPresenter.init('selector-placeholer', onNewItems, onNewItem, applyWrapper, handleError, canCheckStatus);
        });

        $scope.editItem = function (item) {
            console.log(item);

            dialogsSvc.showItemEditDlg(item, function () {
                selectorPresenter.reset();
            });
        };

        $scope.hasComment = function (item) {
            return stringIsNotBlank(item.comment)
                || stringIsNotBlank(item.amount)
                || stringIsNotBlank(item.price);
        };

        $scope.getComment = function (item, short) {
            var str = '';

            if (stringIsNotBlank(item.amount)) {
                str += item.amount;
            }

            if (stringIsNotBlank(item.comment)) {

                if (stringIsNotBlank(str)) {
                    str += ' ';
                }

                str += item.comment;
            }

            return short ? stringGetShort(str, 20) : str;
        };

        $scope.removeItem = function (item) {
            $scope.items.remove(item);
            productSvc.removeProductItem({itemKey: item.id}, function () {

            }, handleError);
            // TODO: refresh list if error
        };

        function handleError(error) {
            $scope.error = error;
        };

        $scope.repeatQuery = function () {
            selectorPresenter.repeatQuery();
        };
    } ]);

