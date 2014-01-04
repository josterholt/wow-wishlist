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
public class HomeController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getHome() {
		
		return "home";
	}
	



	@RequestMapping(value="import-task", method=RequestMethod.GET)
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
	}
}
