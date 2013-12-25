package com.hatraz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.appengine.repackaged.org.codehaus.jackson.type.TypeReference;


import javax.jdo.PersistenceManager;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.Simple;
import com.hatraz.bucketlist.service.PMF;

public class DataImport {
	@SuppressWarnings("unchecked")
	public void run() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File json_file = new File("WEB-INF/data-import/export.json");
		//JsonNode node = mapper.readTree(json_file);
		List<Item> items = mapper.readValue(json_file, new TypeReference<List<Item>>() { });
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistentAll(items);
		pm.close();
		
		/**
		 * Add Indexing
		 */
		SearchService searchService = SearchServiceFactory.getSearchService();
		Index index = searchService.getIndex(IndexSpec.newBuilder().setName("items"));
		searchService.
		for(Item item : items) {
			Document doc = Document.newBuilder()
					.setId((String) item.getId())
					.addField(Field.newBuilder().setName("name").setText(item.getName()))
					.build();
			try {
				index.put(doc);
			} catch(PutException e) {
				System.out.println("An error occurred");
			}
		}
	}
}
