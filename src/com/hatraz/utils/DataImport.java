package com.hatraz.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.ReadPolicy;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceConfig;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;
import com.google.appengine.repackaged.org.codehaus.jackson.type.TypeReference;


import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.service.PMF;

public class DataImport {
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void run(String filename) throws JsonProcessingException, IOException {
		if(filename == null) {
			System.out.println("Bad input specified");
			return;
		}
		FileReader json_file = new FileReader("WEB-INF/data-import/" + filename);
		BufferedReader reader = new BufferedReader(json_file);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ObjectMapper mapper = new ObjectMapper();
		
		double deadline = 60.0;
		
		ReadPolicy readPolicy = new ReadPolicy(ReadPolicy.Consistency.EVENTUAL);
		DatastoreServiceConfig datastoreConfig = DatastoreServiceConfig.Builder.withReadPolicy(readPolicy);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(datastoreConfig);	
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(1);

		String line;
		int i = 0;

		try {		
			while((line = reader.readLine()) != null) {
				Item item = mapper.readValue(line, Item.class);

				
				Query q = new Query("Item");
				System.out.println("Search for id " + item.getId());
				q.setFilter(new FilterPredicate("wowId", FilterOperator.EQUAL, item.getId()));
				PreparedQuery pq = datastore.prepare(q);
				int count = pq.countEntities(fetchOptions);
				System.out.println("Found " + count);
				if(count > 0) {
					System.out.println("Skipping existing record " + item.getId());
				} else {
					if(item != null) {
						pm.makePersistent(item);
						System.out.println("Persisting item " + item.getId());
				       if (i % 10000 == 0)
				        {
				            pm.flush();
				            pm.evictAll();
				        }					
						i++;
					}
				}
			}
		} finally {
			pm.close();
		}
	}

	
	public void sampleData() {
		Item item = new Item();
		item.setName("Testing");
		item.setId(99999);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(item);
	}
	
	public void indexData() {
		Logger log = Logger.getLogger(this.getClass().getPackage().getName());
		
		SearchService searchService = SearchServiceFactory.getSearchService(SearchServiceConfig.newBuilder().setDeadline(120.00).build());
		Index index = searchService.getIndex(IndexSpec.newBuilder().setName("items"));
		

		int max_record = 55086;
		int record_limit = 50;
		int current_offset = 0;
		
		try {
			while(current_offset < max_record) {
				double deadline = 60.0;
	
				ReadPolicy readPolicy = new ReadPolicy(ReadPolicy.Consistency.EVENTUAL);
				DatastoreServiceConfig deadlineConfg = DatastoreServiceConfig.Builder.withDeadline(deadline);		
				DatastoreServiceConfig datastoreConfig = DatastoreServiceConfig.Builder.withReadPolicy(readPolicy).deadline(deadline);
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(datastoreConfig);
				
				FetchOptions fetchOptions = FetchOptions.Builder.withLimit(record_limit).offset(current_offset);
				
				Query q = new Query("Item");
				q.addSort("name");
				PreparedQuery pq = datastore.prepare(q);	
				QueryResultList<Entity>  results = pq.asQueryResultList(fetchOptions);

				ArrayList<Document> docs = new ArrayList<Document>();
				
				for(Entity item : results) {
				
					Document doc = Document.newBuilder()
							.setId((String) item.getProperty("id"))
							.addField(Field.newBuilder().setName("name").setText((String) item.getProperty("name")))
							.build();
					log.info((String) "Adding " + item.getProperty("name"));
					docs.add(doc);
				}

				index.put(docs);
				log.info("Docs put");					
				
				current_offset += record_limit;
			}
		} catch(PutException e) {
			System.out.println("An error occurred");
		}		
	}

		
	@SuppressWarnings("unchecked")
	public Boolean itemExists(Item item) {
		javax.jdo.Query q = PMF.get().getPersistenceManager().newQuery(Item.class);
		q.setFilter("wowId == itemId");
		System.out.println("Checking for " + item.getId());
		List<Item> item_list = (List<Item>) q.execute(item.getId());
		System.out.println(item_list.size());
		if(item_list.size() > 0) {
			return true;
		}
		q.closeAll();
		return false;
		
	}
}
