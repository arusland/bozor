'use strict';

var ItemEditModalCtrl = function ($scope, $modalInstance, item, productSvc) {
    $scope.item = {
        name: item.name,
        amount: item.amount,
        comment: item.comment,
        price: item.price
    };

    $scope.ok = function () {
        $scope.error = '';
        var newItem = jQuery.extend({}, item);
        newItem.amount = $scope.item.amount;
        newItem.comment = $scope.item.comment;
        newItem.price = $scope.item.price;

        productSvc.saveProductItem(newItem, function () {
            item.amount = newItem.amount;
            item.comment = newItem.comment;
            item.price = newItem.price;

            $modalInstance.close(item);
        }, function (error) {
            $scope.error = error.statusText || error;
        });
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};
