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
	$scope.login = {
			"username": "",
			"password": ""
	}
	
	// Login modal added to root scope
	$scope.openMessageBox = function () {
		$("#login-modal").modal({ show: true });
	}
	
	$scope.logout = function () {
		$http.get('/auth/logout').success(function () {
			console.debug("Logged out");
			$scope.isLoggedIn = false;
		});
	}

	/**
	 * Login Required
	 */
	 $scope.$on('event:loginRequired', function () {
	 	$scope.openMessageBox();
	 });
	 
	 $scope.$on('event:loginConfirmed', function () {
		 $scope.isLoggedIn = true;
		 //console.debug("login confirmed");
		 $scope.login.username = "";
		 $scope.login.password = "";
		 $("#login-modal").modal('hide');
	 })
	 
	  $scope.itemFavorites = [];
	  $scope.isFavorited = function (id) {
		  //console.debug("Checking favorited for " + id);
		  //console.debug($scope.itemFavorites);
		  return $scope.itemFavorites.indexOf(id) !== -1;
	  }
	 
	 

	 /*
	  * Checks to see if user is logged in
	  */
	 function ping() {
	 	$http.get('/auth/ping').success(function () {
	 		$scope.$broadcast('event:loginConfirmed');
	 		console.debug('is logged in')
	 		$scope.isLoggedIn = true;
	 	})
	 	.error(function () {
	 		console.debug("foo");
	 	});
	 }
	 ping();	 
}]);