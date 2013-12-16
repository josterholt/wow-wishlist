'use strict';

/* Controllers */

angular.module('bucketlist.controllers', []).
  controller('AppCtrl', ['$scope', '$compile', function ($scope, $compile) {
	  $scope.criteria = null;
	  
	  $scope.myOption = {
	        options: {
	            html: true,
	            focusOpen: false,
	            onlySelect: true,
	            source: function (request, response) {
	            	console.debug(request);
	                var data = [
	                        "Asp",
	                        "BASIC",
	                        "C",
	                        "C++",
	                        "Clojure",
	                        "COBOL",
	                        "ColdFusion",
	                        "Erlang",
	                        "Fortran",
	                        "Groovy",
	                        "Haskell",
	                        "Java",
	                        "JavaScript",
	                        "Lisp",
	                        "Perl",
	                        "PHP",
	                        "Python",
	                        "Ruby",
	                        "Scala",
	                        "Scheme"
	                ];
	                data = $scope.myOption.methods.filter(data, request.term);
	
	                if (!data.length) {
	                    data.push({
	                        label: 'not found',
	                        value: ''
	                    });
	                }
	                // add "Add Language" button to autocomplete menu bottom
	                response(data);

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