'use strict';

bozorApp.controller('PieChartByProductTypeByMonthController', [ '$scope', 'chartHelper', '$routeParams',
    function ($scope, chartHelper, $routeParams) {
        chartHelper.init($scope, $routeParams, 'pmt', $('#_costForTypePeriod').val());
    }]);
