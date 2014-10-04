'use strict';

bozorApp.factory('productSvc', ['$resource', function ($resource) {

    return $resource('',
        { }, {
            listProducts: {
                url: '/api/products',
                isArray: true,
                method: 'GET'
            },
            listProductItems: {
                url: '/api/items/:time',
                params: { time: '@time' },
                isArray: true,
                method: 'GET'
            },
            listMonthItems: {
                url: '/api/itemsMonth/:month',
                params: { month: '@month' },
                isArray: true,
                method: 'GET'
            },
            saveProduct: {
                url: '/api/product',
                method: 'POST'
            },
            saveProductItem: {
                url: '/api/item',
                method: 'POST'
            },
            removeProductItem: {
                url: '/api/item/:itemKey',
                params: { itemKey: '@itemKey' },
                method: 'DELETE'
            },
            getStatus: {
                url: '/api/status/:token/:time',
                params: { token: '@token', time: '@time' },
                method: 'GET'
            },
            buyProductItem: {
                url: '/api/buy/:key',
                params: { key: '@key' },
                method: 'POST'
            }
        });
}]);