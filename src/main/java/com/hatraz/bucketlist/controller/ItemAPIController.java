package com.hatraz.bucketlist.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.repository.ItemRepo;
import com.hatraz.bucketlist.service.ItemService;
//import com.hatraz.utils.DataImport;

@Controller
public class ItemAPIController {
	//@Autowired ItemService itemService;
	@Autowired ItemRepo itemService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/api/items", method=RequestMethod.GET)
	public @ResponseBody List<Item> searchItems(@RequestParam String name) {
		System.out.println("searchItems");
		Pageable topTen = new PageRequest(0, 10);
		List<Item> items = itemService.searchByName("%" + name + "%", topTen);
		return items;
	}
	
	@RequestMapping(value="/api/items/{id}", method=RequestMethod.GET)
	public @ResponseBody Item getItem(@PathVariable(value="id") String id) throws EntityNotFoundException {
		System.out.println("getItem");
		//DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		//Entity item = datastore.get(id);
		return itemService.findOne(Integer.parseInt(id));
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
