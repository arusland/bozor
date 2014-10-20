'use strict';

bozorApp.controller('PieChartByProductByMonthController', [ '$scope', 'chartHelper', '$routeParams',
    function ($scope, chartHelper, $routeParams) {
        chartHelper.init($scope, $routeParams, 'pmp', $('#_costForPeriod').val());
    }]);
