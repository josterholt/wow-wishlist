'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('bucketlist.services', ['ngResource']).
	factory('Item', function ($resource) {
		var Item = $resource('/api/items/:id', 
			{ type: 'Item' },
			{ 
				update: { method: 'POST'},
				search: {method: 'GET', isArray: true},
				get: {method: 'GET'}
			}
		);

		Item.prototype.update = function (cb) {
			return Item.update({id: this._id.$oid},
				angular.extend({}, this, {_id: undefined}), cb);
		};

		Item.prototype.destroy = function (cb) {
			return Item.remove({id: this._id.$oid}, cb);
		};
		return Item;
	}).
	service('Auth', function ($rootScope, $http, Base64) {
		//auth_string = Base64.encode($scope.username + ':' + $scope.password)
		//return $resource('/auth/login', {}, { login: {headers: {Authorization: auth_string}, method: 'POST'} });
		
		return {
			"login": function(username, password) {
				var auth_string = "Basic " + Base64.encode(username + ':' + password);
				
				return $http({
					method: "POST",
					url: "/auth/login",
					headers: {"Authorization": auth_string}
				})
				.success(function () {
			 		$rootScope.$broadcast('event:loginConfirmed');
			 		console.debug('is logged in service')
				})
			}
		}
	}).
	service('WishList', function ($rootScope, $http) {
		
		  
		var WishList = {
			items: [],
			getItems: function () {
				$http.get("/api/favorites/")
			  	.then(function (response) {  		
			  		for(var i in response.data) {
			  		    $rootScope.itemFavorites.push(response.data[i].id);
			  		}
			  		WishList.items = response.data;
			  });
			},
			getItem: function(id) {
				return items[0];
			},
			addFavorite: function (id) {
				  $http.get("/api/favorite/" + id)
				  .then(function () {
					  if($rootScope.itemFavorites.indexOf(id) == -1) {
						  $rootScope.itemFavorites.push(id);
					  }
				  });
			  },			  
			  removeFavorite: function (id) {
				  $http.get("/api/favorite/" + id + "/delete")
				  	.then(function () {
					  var idx = $rootScope.itemFavorites.indexOf(id)
					  $rootScope.itemFavorites.splice(idx, 1);
				  });		  
			  },	
			sortableOptions: {
				  stop: function(e, ui) {
					  console.debug("Update sorting");
				  },
			  }
		}
		
	  return WishList;
	}).
	factory('Base64', function () {
	    /* jshint ignore:start */
	  
	    var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
	  
	    return {
	        encode: function (input) {
	            var output = "";
	            var chr1, chr2, chr3 = "";
	            var enc1, enc2, enc3, enc4 = "";
	            var i = 0;
	  
	            do {
	                chr1 = input.charCodeAt(i++);
	                chr2 = input.charCodeAt(i++);
	                chr3 = input.charCodeAt(i++);
	  
	                enc1 = chr1 >> 2;
	                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
	                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
	                enc4 = chr3 & 63;
	  
	                if (isNaN(chr2)) {
	                    enc3 = enc4 = 64;
	                } else if (isNaN(chr3)) {
	                    enc4 = 64;
	                }
	  
	                output = output +
	                    keyStr.charAt(enc1) +
	                    keyStr.charAt(enc2) +
	                    keyStr.charAt(enc3) +
	                    keyStr.charAt(enc4);
	                chr1 = chr2 = chr3 = "";
	                enc1 = enc2 = enc3 = enc4 = "";
	            } while (i < input.length);
	  
	            return output;
	        },
	  
	        decode: function (input) {
	            var output = "";
	            var chr1, chr2, chr3 = "";
	            var enc1, enc2, enc3, enc4 = "";
	            var i = 0;
	  
	            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
	            var base64test = /[^A-Za-z0-9\+\/\=]/g;
	            if (base64test.exec(input)) {
	                window.alert("There were invalid base64 characters in the input text.\n" +
	                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
	                    "Expect errors in decoding.");
	            }
	            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
	  
	            do {
	                enc1 = keyStr.indexOf(input.charAt(i++));
	                enc2 = keyStr.indexOf(input.charAt(i++));
	                enc3 = keyStr.indexOf(input.charAt(i++));
	                enc4 = keyStr.indexOf(input.charAt(i++));
	  
	                chr1 = (enc1 << 2) | (enc2 >> 4);
	                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
	                chr3 = ((enc3 & 3) << 6) | enc4;
	  
	                output = output + String.fromCharCode(chr1);
	  
	                if (enc3 != 64) {
	                    output = output + String.fromCharCode(chr2);
	                }
	                if (enc4 != 64) {
	                    output = output + String.fromCharCode(chr3);
	                }
	  
	                chr1 = chr2 = chr3 = "";
	                enc1 = enc2 = enc3 = enc4 = "";
	  
	            } while (i < input.length);
	  
	            return output;
	        }
	    }
	}).	
	value('version', '0.1');