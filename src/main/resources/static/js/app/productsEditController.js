'use strict';

bozorApp.controller('ProductsEditController', [ '$scope', 'productSvc', '$timeout',
    function ($scope, productSvc, $timeout) {
        $scope.products = [];
        $scope.productTypes = [];
        $scope.tempProduct = null;
        $scope.currentProduct = null;

        productSvc.listProductTypes({}, fillProductTypes, handleError);

        $scope.saveProduct = function () {
            if ($scope.currentProduct != null && $scope.tempProduct != null){
                $scope.currentProduct.name = $scope.tempProduct.name;
                $scope.currentProduct.typeId = $scope.tempProduct.typeId;
                updateSelectedType($scope.currentProduct);
            }

            $scope.cancelEdit();
        };

        $scope.startEdit = function(product){
            $scope.cancelEdit();

            product.edit = true;
            $scope.currentProduct = product;
            $scope.tempProduct =
            {
                id: product.id,
                name: product.name,
                typeId: product.typeId
            };
            createSelector(product);
        };

        $scope.cancelEdit = function(){
            if ($scope.currentProduct) {
                $scope.currentProduct.edit = false;
                $scope.currentProduct = null;
            }
            $scope.tempProduct = null;
        };

        $scope.removeProduct = function (product) {
            $scope.products.remove(product);

            /*productSvc.removeProduct({itemKey: item.id}, function () {

             }, handleError);*/
        };

        function fillProducts(products) {
            $scope.products = products;

            angular.forEach(products, function (product) {
                updateSelectedType(product);
            });
        };

        function fillProductTypes(productTypes) {
            $scope.productTypes = productTypes;

            productSvc.listProducts({}, fillProducts, handleError);
        };

        function handleError(error) {
           // alert(error);
            console.log(error);
            $scope.error = error;
        };

        function updateSelectedType(product) {
            product.selectedType = getSelectedType(product);
        }

        function getSelectedType(product){
            for(var key in $scope.productTypes){
                var item = $scope.productTypes[key];
                if (item.id === product.typeId){
                    return item;
                }
            }

            return null;
        };

        function createSelector(product){
            var host = 'prod' + product.id
            var selector = host + 'sel';
            var hostElem = $("#" + host);
            hostElem.empty();

            $('<select style="width:90%" id="' + selector + '" data-placeholder="' + strGetWhatelse() +
                '" tabindex="1">' + createTypeOptions(product) + '</select>')
                .appendTo(hostElem);

            $('#'+selector).chosen({create_option: onCreateProductType,
                skip_no_results: true, create_option_text: 'Add product type', width: '100%'});

            $('#'+selector).on('change', function (evt, params) {
                $scope.tempProduct.typeId = parseInt(params.selected);
            });
        };

        function createTypeOptions(product){
            var result = '';

            angular.forEach($scope.productTypes, function (productType) {
                if (product.typeId !== productType.id) {
                    result += '<option value="' + productType.id + '">' + productType.name + '</option>';
                } else {
                    result += '<option selected="" value="' + productType.id + '">' + productType.name + '</option>';
                }
            });

            return result;
        }

        function onCreateProductType(param){
            var newType =
            {
                name: param
            };

            productSvc.saveProductType(newType, function (savedType) {
                if ($scope.tempProduct) {
                    $scope.productTypes.push(savedType);
                    $scope.tempProduct.typeId = savedType.id;
                    createSelector($scope.tempProduct);
                }
            }, handleError);
        };
    } ]);

