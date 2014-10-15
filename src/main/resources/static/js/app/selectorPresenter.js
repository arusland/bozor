'use strict';

bozorApp.factory('selectorPresenter', ['productSvc', '$timeout', 'dialogsSvc',
    function (productSvc, $timeout, dialogsSvc) {
        var lastToken = 0;
        var time;
        var handleErrorCaller;
        var onNewItem;
        var onNewItems;
        var inited = false;
        var currentProducts = null;
        var canCheckStatus;
        var WORK_MIN_TIMEOUT = 1000;
        var WORK_TIMEOUT = 5000;
        var WORK_MAX_TIMEOUT = 60000;

        $timeout(onTimeout, WORK_TIMEOUT);

        var init = function (id, _onNewItems, _onNewItem, applyWrapper, _handleError, _canCheckStatus, _time) {
            $('<select style="width:100%" id="selector" data-placeholder="' + strGetWhatelse() +
                '" tabindex="1" multiple=""><option value=""></option></select>')
                .appendTo($("#" + id));

            $('#selector').chosen({create_option: onCreateProduct, skip_no_results: true,
                create_option_text: 'Добавить продукт'});

            $('#selector').on('change', function (evt, params) {
                var selected = $('#selector :selected');
                var name = selected.text();
                selected.removeAttr('selected');
                $('#selector').trigger('chosen:updated');

                applyWrapper(function () {
                    onAddProductItem(params.selected, name);
                });
            });

            handleErrorCaller = _handleError;
            onNewItem = _onNewItem;
            onNewItems = _onNewItems;
            time = _time || '';
            canCheckStatus = _canCheckStatus || function() {return true;};
            lastToken = 0;
            currentProducts = null;
            inited = true;
            repeatQuery();
        };

        function onTimeout(skipTimer) {
            if (!inited || !canCheckStatus()) {
                $timeout(function () {
                    onTimeout(skipTimer)
                }, WORK_MIN_TIMEOUT);

                return;
            }

            productSvc.getStatus({token: lastToken, time: time}, function (data) {
                lastToken = data.token;
                handleErrorCaller(null);

                if (data.updates === undefined){
                    handleErrorCaller('Loading data failed');
                    return;
                }

                if (data.updates.items) {
                    onNewItems(data.updates.items);
                }

                if (data.updates.products) {
                    fillProducts(data.updates.products);
                }

                if (!skipTimer) {
                    $timeout(onTimeout, WORK_TIMEOUT);
                }
            }, handleError);
        };

        function onCreateProduct(param) {
            productSvc.saveProduct({name: param}, function (data) {
                listProducts(function () {
                    onAddProductItem(data.id, param);
                });
            }, handleError);
        };

        function onAddProductItem(parentId, name) {
            var item = {
                name: name,
                productId: parentId,
                comment: null,
                amount: null,
                price: null
            };

            onNewItem(item);
            productSvc.saveProductItem(item, function (data) {
                item.id = data.id;
            }, handleError);
            //TODO: refresh list if error
        };

        function fillProducts(data) {
            if (productsAreDifferent(data, currentProducts)) {
                currentProducts = data;

                var selector = $('#selector')
                    .find('option')
                    .remove()
                    .end();

                if (data) {

                    angular.forEach(data, function (value, key) {
                        selector.append($("<option></option>")
                            .attr("value", value.id)
                            .text(value.name));
                    });
                }

                selector.trigger('chosen:updated');
            }
        };

        function listProducts(handler) {
            productSvc.listProducts(function (data) {
                fillProducts(data);

                if (handler) {
                    handler();
                }
            }, handleError)
        };

        function handleError(error) {
            error = (error ? error.statusText : '');

            if (!error) {
                error = 'Loading failed';
            }

            handleErrorCaller(error);

            $timeout(onTimeout, WORK_MAX_TIMEOUT);
        };

        function repeatQuery() {
            reset();
            handleErrorCaller(null);
            onTimeout(true);
        };

        function reset(){
            lastToken = 0;
            currentProducts = null;
        };

        function stop(){
            reset();
            inited = false;
        };

        return {
            init: init,
            repeatQuery: repeatQuery,
            reset: reset,
            stop: stop
        };
    }]);
