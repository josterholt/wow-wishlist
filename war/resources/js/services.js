'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('bucketlist.services', ['ngResource']).
	factory('Item', function ($resource) {
		var Item = $resource('/api/items/:id', 
			{ type: 'Item' },
			{ 
				update: { method: 'POST' },
				query: {method: 'GET', isArray: true }
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