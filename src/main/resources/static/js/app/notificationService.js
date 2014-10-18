'use strict';

bozorApp.factory('notification',
    function () {
        toastr.options = {
            "closeButton": true,
            "positionClass": "toast-top-right",
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "50000",
            "extendedTimeOut": "10000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };

        function error(msg, title) {
            toastr.error(msg, title);
        }

        function info(msg, title) {
            toastr.info(msg, title);
        }

        function success(msg, title) {
            toastr.success(msg, title);
        }

        function warning(msg, title) {
            toastr.warning(msg, title);
        }

        return {
            success: success,
            error: error,
            info: info,
            warning: warning
        };
    });