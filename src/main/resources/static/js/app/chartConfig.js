'use strict';

bozorApp.config(function ($routeProvider) {
    $routeProvider
        .when('/pmp:month',
        {
            controller: 'PieChartByProductByMonthController',
            templateUrl: '/partials/pieByMonth'
        })
        .when('/pmt:month',
        {
            controller: 'PieChartByProductTypeByMonthController',
            templateUrl: '/partials/pieByMonth'
        })
        .when('/pmt',
        {
            redirectTo: '/pmt' + formatShortMonth(new Date())
        })
        .otherwise({ redirectTo: '/pmp' + formatShortMonth(new Date()) });
});
