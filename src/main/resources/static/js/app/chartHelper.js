'use strict';

bozorApp.factory('chartHelper', ['productSvc',
    function (productSvc) {
        function init($scope, $routeParams, prefix, title) {
            moment.locale(getCurrentLocale());
            var month = moment($routeParams.month, _shortMonthFormat);
            if (!month.isValid()) {
                month = moment();
                $routeParams.month = month.format(_shortMonthFormat);
            }
            $scope.commands = getMonthCommands(month, prefix);
            var title = title + ' ' + getMonthName(month).toLowerCase();
            var prevMonth = month.clone().subtract(1, 'month').format(_shortMonthFormat);
            var nextMonth = month.clone().add(1, 'month').format(_shortMonthFormat);
            $('#prevDay').attr('href', '#' + prefix + prevMonth);
            $('#nextDay').attr('href', '#' + prefix + nextMonth);
            var methodName = 'pmp' === prefix ?
                'getPieChartDataByProductInMonth' : 'getPieChartDataByProductTypeInMonth';

            productSvc[methodName]({month: $routeParams.month}, function (data) {
                drawChart(data, title, $('#_wholePrice').val());
            }, function () {
                alert('Load items failed')
            });
        }

        function drawChart(data, title, totalPrice) {
            // Build the chart
            $('#charthost').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false
                },
                title: {
                    text: title
                },
                tooltip: {
                    pointFormat: totalPrice + ': <b>{point.y}</b> ({point.percentage:.1f}%)'
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

        return {
            init: init
        };
    }]);


