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
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/home', {templateUrl: 'resources/partials/home.html', controller: 'HomeCtrl'});
  $routeProvider.when('/search', {templateUrl: 'resources/partials/search.html', controller: 'SearchCtrl'});
  $routeProvider.otherwise({redirectTo: '/home'});
}]);