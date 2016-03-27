'use strict';


// Declare app level module which depends on filters, and services
angular.module('bucketlist', [
  'ngRoute',
  'ui.sortable',
  'ui.autocomplete',
  'bucketlist.filters',
  'bucketlist.services',
  'bucketlist.directives',
  'bucketlist.controllers'
]).
config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
  $routeProvider
  	.when('/home', {templateUrl: 'resources/partials/home.html', controller: 'HomeCtrl'})
  	.when('/search', {templateUrl: 'resources/partials/search.html', controller: 'SearchCtrl' })
  	.when('/item/:id', { templateUrl: 'resources/partials/item.html', controller: 'ItemDetailCtrl' })
  	.otherwise({redirectTo: '/home'});
  
  	//Interceptor
	var interceptor = ['$rootScope', '$q', function($scope, $q) {
		function success(response) {
			return response;
		}

		function error(response) {
			var status = response.status;
			if(status == 401) {
				var deferred = $q.defer();
				var req = {
					config: response.config,
					deferred: deferred
				}

				$scope.requests401.push(req);
				$scope.$broadcast('event:loginRequired');
				return deferred.promise;
			}

			return $q.reject(response);
		}

		return function(promise) {
			return promise.then(success, error);
		}
	}];
	$httpProvider.responseInterceptors.push(interceptor);
}])
.run(['$rootScope', '$http', '$location', function($scope, $http, $location) {
	$scope.requests401 = [];
	$scope.isLoggedIn = false;
	
	// Login modal added to root scope
	$scope.openMessageBox = function () {
		$("#login-modal").modal({ show: true });
	}
	
	$scope.logout = function () {
		$http.get('/api/logout').success(function () {
			$scope.isLoggedIn = false;
		});
	}

	/**
	 * Login Required
	 */
	 $scope.$on('event:loginRequired', function () {
	 	$scope.openMessageBox();
	 });
	 

	 /*
	  * Checks to see if user is logged in
	  */
	 function ping() {
	 	$http.get('/api/ping').success(function () {
	 		$scope.$broadcast('event:loginConfirmed');
	 		console.debug('is logged in')
	 		$scope.isLoggedIn = true;
	 	});
	 }
	 ping();	 
}]);