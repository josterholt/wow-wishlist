'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('bucketlist.services', []).
	service('WishList', function () {
		var items = [
             { 
            	 name: 'Test 1',
            	 type: 'ACHIEVEMENT'
    		 }, 
    		 {
    			 name: 'Test 2',
    			 type: 'ITEM'
			 },
    		 {
    			 name: 'Test 3',
    			 type: 'SET'
			 }
		 ];
		
		// @todo code out function
		var getItem = function (id) {
			return items[0];
		};

		// @todo code out function		
		var getItems = function (criteria) {
			return items;
		};
		
		return { 
			items: items,
			getItem: getItem,
			getItems: getItems
		};
	}).
	value('version', '0.1');