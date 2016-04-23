'use strict';

/* Controllers */

angular.module('bucketlist.controllers', []).
  controller('AppCtrl', ['$scope', '$compile', '$location', 'Item', function ($scope, $compile, $location, Item) {
	  $scope.criteria = null;
	  $scope.submitSearch = function () {
		  console.debug("Search submitted");
	  }

	  $scope.myOption = {
	        options: {
	            html: true,
	            focusOpen: false,
	            onlySelect: true,
	            source: function (request, response) {
	            	Item.search({ 'name': request.term }, function (data) {
	            		data.$promise.then(function (results) {
	            			var data = [];
			                if (!results.length) {
			                    data.push({
			                        label: 'not found',
			                        value: ''
			                    });
			                } else {
			                	for(var i =0; i < results.length; i++) {
			                		data.push({ 
			                			id: results[i].id,
			                			label: results[i].name,
			                			value: results[i].name
		                			});	                		
			                	}
			                }
			                // add "Add Language" button to autocomplete menu bottom
			                response(data);	            	
	            		});
	            	});
	            }
	        },
	        methods: {},
	        events: {
		        select: function (event, ui) {
		        	$location.path('/item/' + ui.item.id);
		        }
	        }
	  };
	  
	  $scope.search = {
          type: null,
		  options: {
		      types: [
			      {
                      name: 'Achievement',
	        		  value: 'ACHIEVEMENT'
	        	  },
	        	  {
                      name: 'Item',
	        		  value: 'ITEM'
	        	  }
	          ]
          }
	  }
  }]).
  controller('HomeCtrl', ['$scope', 'WishList', '$http', function($scope, WishList, $http) {
	  $scope.WishList = WishList;
	  $scope.WishList.getItems();
  }])
  .controller('SearchCtrl', ['$routeParams', '$scope', function($routeParams, $scope) {
	  $scope.items = Item.search({ 'name': request.term });
  }])
  .controller('ItemDetailCtrl', ['$scope', '$routeParams', 'Item', '$http', '$timeout', 'WishList', function($scope, $routeParams, Item, $http, $timeout, WishList) {
	  $scope.WishList = WishList;
	  $http.get("/api/getFavoriteIDs?ids=" + $routeParams.id)
	  	.then(function (items) {
	  		$timeout(function () {
		  		for(var i in items.data) {
		  			$scope.itemFavorites.push(items.data[i])
		  		}
	  		}, 0)
	  	})
	  
	  $scope.item = Item.get({ id: $routeParams.id });
  }]);