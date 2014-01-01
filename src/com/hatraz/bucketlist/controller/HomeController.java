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

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.repackaged.org.codehaus.jackson.JsonProcessingException;
import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.service.PMF;
import com.hatraz.utils.DataImport;

@Controller
public class HomeController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getHome() {
		Item item = new Item();
		item.setName("Testing");

		PMF.get().getPersistenceManager().makePersistent(item);
		System.out.println("Test persistence");
		
		return "home";
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/items", method=RequestMethod.GET)
	public @ResponseBody Collection<ScoredDocument> searchItems(@RequestParam String name) {	
		//PersistenceManager pm = PMF.get().getPersistenceManager();
		//Query q = pm.newQuery(Item.class);
		//q.setFilter(":p1.contains(name)");
		String query_string = "name: " + name;
		
		SearchService searchService = SearchServiceFactory.getSearchService();
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("items").build();
		Index index = searchService.getIndex(indexSpec);
		Results<ScoredDocument> results = index.search(query_string);
		
		System.out.println(results.getResults().size());
		
		ArrayList<ScoredDocument> items = new ArrayList<ScoredDocument>();
		Iterator<ScoredDocument> iterator = results.getResults().iterator();
		while(iterator.hasNext()) {
			ScoredDocument doc = iterator.next();
			System.out.println(doc.getFields("name"));
			items.add(doc);
		}
		
		return items;
	}
	
	
	@RequestMapping(value="import-data", method=RequestMethod.GET)
	public String importData() throws JsonProcessingException, IOException {
		DataImport di = new DataImport();
		di.run();
		//Item item = new Item();
		//item.setId(59612);
		//System.out.println(di.itemExists(item));
		return "home";
	}
	
	@RequestMapping(value="index-data", method=RequestMethod.GET)
	public String indexData() {
		DataImport di = new DataImport();
		di.indexData();
		return "home";
	}
}
