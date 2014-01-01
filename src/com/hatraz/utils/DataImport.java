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
	@SuppressWarnings("unchecked")
	public void run() throws JsonProcessingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		FileReader json_file = new FileReader("WEB-INF/data-import/export.json");
		BufferedReader reader = new BufferedReader(json_file);

		SearchService searchService = SearchServiceFactory.getSearchService();
		
		Index index = searchService.getIndex(IndexSpec.newBuilder().setName("items"));

		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Transaction tx = pm.currentTransaction();
		//tx.begin();
		
		String line;
		int i = 0;
		try {		
			while((line = reader.readLine()) != null) {
				Item item = mapper.readValue(line, Item.class);
						
				if(item != null) {
					pm.makePersistent(item);
			       if (i % 10000 == 0)
			        {
			            pm.flush();
			            pm.evictAll();
						//System.out.println("Managing " + pm.getManagedObjects().size() + " items");
			        }					
	
			       /*
					try {
						Document doc = Document.newBuilder()
								.setId(Integer.toString(item.getId()))
								.addField(Field.newBuilder().setName("name").setText(item.getName()))
								.build();
						index.put(doc);
					} catch(PutException e) {
						System.out.println("An error occurred");
					}
					*/

					i++;
				}
			}
			//tx.commit();
		} finally {
			//if(tx.isActive()) {
				//tx.rollback();
			//}
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
				double deadline = 120.0;
	
				ReadPolicy readPolicy = new ReadPolicy(ReadPolicy.Consistency.EVENTUAL);
				DatastoreServiceConfig deadlineConfg = DatastoreServiceConfig.Builder.withDeadline(deadline);		
				DatastoreServiceConfig datastoreConfig = DatastoreServiceConfig.Builder.withReadPolicy(readPolicy).deadline(deadline);
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(datastoreConfig);
				
				FetchOptions fetchOptions = FetchOptions.Builder.withLimit(record_limit).offset(current_offset);
				//fetchOptions.startCursor(Cursor.fromWebSafeString(String.valueOf(current_offset)));
				
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
