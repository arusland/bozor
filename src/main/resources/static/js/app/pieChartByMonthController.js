'use strict';

bozorApp.controller('PieChartByMonthController', [ '$scope', 'productSvc', '$routeParams',
    function ($scope, productSvc, $routeParams) {
        init();

        function init() {
            $scope.modified = false;
            moment.locale(getCurrentLocale());
            var month = moment($routeParams.month, _shortMonthFormat);
            if (!month.isValid()) {
                month = moment();
                $routeParams.month = month.format(_shortMonthFormat);
            }
            $scope.monthName = getMonthName(month);
            $scope.commands = getMonthCommands(month, 'pm');
            $scope.wholePrice = $('#_wholePrice').val();
            $scope.costForPeriod = $('#_costForPeriod').val();
            var prevMonth = month.clone().subtract(1, 'month').format(_shortMonthFormat);
            var nextMonth = month.clone().add(1, 'month').format(_shortMonthFormat);
            $('#prevDay').attr('href', '#pm' + prevMonth);
            $('#nextDay').attr('href', '#pm' + nextMonth);
            productSvc.getPieChartDataByMonth({month: $routeParams.month}, function (data) {
                drawChart(data);
            }, function () {
                alert('Load items failed')
            });
        }

        function drawChart(data) {
            // Build the chart
            $('#charthost').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: $scope.costForPeriod + ' ' + $scope.monthName.toLowerCase()
                },
                tooltip: {
                    pointFormat: $scope.wholePrice + ': <b>{point.y}</b> ({point.percentage:.1f}%)'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f}%',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            },
                            connectorColor: 'silver'
                        }
                    }
                },
                series: [
                    {
                        type: 'pie',
                        data: data
                    }
                ]
            });
        }
    }]);
