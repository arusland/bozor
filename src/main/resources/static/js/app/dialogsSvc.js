'use strict';

bozorApp.factory('dialogsSvc', ['$modal', 'productSvc', function ($modal, productSvc) {
    var _dialogIsShown = false;

    var showItemEditDlg = function (item, callback, onCancel) {
        var modalInstance = modalOpen('itemEditModal', 'ItemEditModalCtrl', {
            resolve: {
                item: function () {
                    return item;
                },
                productSvc: function () {
                    return productSvc;
                }
            }
        });

        modalInstance.result.then(function (item) {
            _dialogIsShown = false;

            if (callback) {
                callback(item);
            }
        }, function () {
            _dialogIsShown = false;

            if (onCancel) {
                onCancel();
            }
        });
    };

    var dialogIsShown = function(){
        return _dialogIsShown;
    }

    var modalOpen = function (tmpl, ctrl, options) {
        options.templateUrl = '/partials/' + tmpl;
        options.controller = ctrl;
        if (!options.easyClose) {
            options.backdrop = 'static';
            options.keyboard = false;
        }

        _dialogIsShown = true;

        return $modal.open(options);
    };

    return {
        showItemEditDlg: showItemEditDlg,
        dialogIsShown: dialogIsShown
    };
}]);
