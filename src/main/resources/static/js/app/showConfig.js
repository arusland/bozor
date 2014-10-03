'use strict';

bozorApp.config(function ($routeProvider) {
    $routeProvider
        .when('/d:time',
        {
            controller: 'ShowDayController',
            templateUrl: '/partials/showByDay'
        })
        .when('/m:month',
        {
            controller: 'ShowMonthController',
            templateUrl: '/partials/showByMonth'
        })
        .otherwise({ redirectTo: '/d' + formatShortDate(new Date()) });
});
