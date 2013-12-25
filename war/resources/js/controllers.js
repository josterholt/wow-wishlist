'use strict';

/* Controllers */

angular.module('bucketlist.controllers', []).
  controller('AppCtrl', ['$scope', '$compile', 'Item', function ($scope, $compile, Item) {
	  $scope.criteria = null;
	  
	  $scope.myOption = {
	        options: {
	            html: true,
	            focusOpen: false,
	            onlySelect: true,
	            source: function (request, response) {
	            	Item.query({ 'name': request.term }, function (data) {
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
			                			label: results[i].fields[0].text,
			                			value: results[i].id
		                			});	                		
			                	}
			                }
			                // add "Add Language" button to autocomplete menu bottom
			                response(data);	            	
	            		});
	            	});
	            }
	        },
	        methods: {}
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
  .controller('SearchCtrl', [function() {

  }]);