'use strict';

/* Directives */


angular.module('bucketlist.directives', []).
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }])
  .directive('loginform',['Auth', function(Auth) {
	  return {
		  transclude: true,
		  scope: {
			  username: '@',
			  password: '@',
		  },
		  templateUrl: '/resources/partials/components/modals/login.html',
		  link: function (scope) {
			  scope.username = '';
			  scope.password = '';
			  scope.login = function () {
				  Auth.login(scope.username, scope.password)
				  .success(function () {
					  scope.username = '';
					  scope.password = '';
				  })
			  }
			  
		  }
	  };
  }]);