package com.hatraz.bucketlist.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.service.ItemService;
import com.hatraz.bucketlist.service.PMF;
//import com.hatraz.utils.DataImport;

@Controller
public class ItemAPIController {
	@Autowired ItemService itemService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/items", method=RequestMethod.GET)
	public @ResponseBody List<Item> searchItems(@RequestParam String name) {	
		List<Item> items = itemService.searchByName(name);
		return items;
	}
	
	@RequestMapping(value="/api/items/{id}", method=RequestMethod.GET)
	public Entity getItem(@RequestParam String id) throws EntityNotFoundException {
		//DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Entity item = datastore.get(id);
		
		return null;
	}
	



/*	@RequestMapping(value="import-task", method=RequestMethod.GET)
	public String importTask() {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/import-data?filename=export.a.json").method(Method.GET));
		return "complete";
	}
	
	@RequestMapping(value="import-data", method=RequestMethod.GET)
	public String importData(@RequestParam String filename) throws JsonProcessingException, IOException {
		DataImport di = new DataImport();
		di.run(filename);
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
	}*/
}