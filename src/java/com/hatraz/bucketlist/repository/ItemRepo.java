package com.hatraz.bucketlist.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.service.PMF;

public class ItemRepo {
	public static List<Item> getItems(List<String> filters, List<Object> values) {
/*		javax.jdo.Query q = PMF.get().getPersistenceManager().newQuery(Item.class);

		for(String filter : filters) {
			q.setFilter(filter);
		}
		
		@SuppressWarnings("unchecked")
		List<Item> items = (List<Item>) q.execute(values);
		q.closeAll();
		return items;	*/
		return null;
	}
}
