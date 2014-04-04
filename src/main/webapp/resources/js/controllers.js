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
	            	Item.query({ 'name': request.term }, function (data) {
	            		data.$promise.then(function (results) {
	            			console.debug(results);
	            			var data = [];
			                if (!results.length) {
			                    data.push({
			                        label: 'not found',
			                        value: ''
			                    });
			                } else {
			                	for(var i =0; i < results.length; i++) {
			                		console.debug(results[i]);
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
  controller('HomeCtrl', ['$scope', 'WishList', function($scope, WishList) {
	  $scope.WishList = WishList;
  }])
  .controller('SearchCtrl', ['$routeParams', '$scope', function($routeParams, $scope) {
	  $scope.items = Item.query({ 'name': request.term });
  }])
  .controller('ItemDetailCtrl', ['$scope', '$routeParams', 'Item', function($scope, $routeParams, Item) {
	  console.debug('Item detail');
	  console.debug($routeParams.id);
	  $scope.item = Item.query({ id: $routeParams.id });
  }]);