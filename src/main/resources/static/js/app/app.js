var bozorApp = angular.module('bozorApp', ['ngResource', 'ui.bootstrap.modal', 'ngRoute']);

bozorApp.config(['$httpProvider', function ($httpProvider) {
    // Intercept POST requests, convert to standard form encoding
    $httpProvider.defaults.headers.common["Bzr-TimeOffset"] = new Date().getTimezoneOffset().toString();
}]);
