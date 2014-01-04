package com.hatraz.bucketlist.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.google.appengine.repackaged.org.codehaus.jackson.JsonProcessingException;
import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.service.PMF;
import com.hatraz.utils.DataImport;

@Controller
public class ItemAPIController {
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/items", method=RequestMethod.GET)
	public @ResponseBody Collection<ScoredDocument> searchItems(@RequestParam String name) {	
		String query_string = "name: " + name;
		
		SearchService searchService = SearchServiceFactory.getSearchService();
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("items").build();
		Index index = searchService.getIndex(indexSpec);
		Results<ScoredDocument> results = index.search(query_string);
		
		ArrayList<ScoredDocument> items = new ArrayList<ScoredDocument>();
		Iterator<ScoredDocument> iterator = results.getResults().iterator();
		while(iterator.hasNext()) {
			ScoredDocument doc = iterator.next();

			System.out.println(doc.getFields("name"));
			items.add(doc);
		}
		
		return items;
	}
	
	@RequestMapping(value="/api/items/{id}", method=RequestMethod.GET)
	public Entity getItem(@RequestParam String id) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Entity item = datastore.get(id);
		
		return item;
	}
}
