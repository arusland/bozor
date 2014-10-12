'use strict';

bozorApp.config(function ($routeProvider) {
    $routeProvider
        .when('/pm:month',
        {
            controller: 'PieChartByMonthController',
            templateUrl: '/partials/pieByMonth'
        })
        .otherwise({ redirectTo: '/pm' + formatShortMonth(new Date()) });
});
