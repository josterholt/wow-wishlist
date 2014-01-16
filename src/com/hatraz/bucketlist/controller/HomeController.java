package com.hatraz.bucketlist.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;



import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.ReadPolicy;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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
import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.repository.UserRepo;
import com.hatraz.bucketlist.service.PMF;
import com.hatraz.bucketlist.service.UserDetailServiceImpl;
import com.hatraz.utils.DataImport;


@Controller
//@SessionAttributes("{UserSession}")
@SessionAttributes({"UserID", "twitter", "requestToken"})
public class HomeController {
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getHome(ModelMap model) {
		return "home";
	}

	@RequestMapping(value="/create-user", method=RequestMethod.GET)
	public String setupUser(ModelMap modelMap) {
		ReadPolicy readPolicy = new ReadPolicy(ReadPolicy.Consistency.EVENTUAL);
		DatastoreServiceConfig datastoreConfig = DatastoreServiceConfig.Builder.withReadPolicy(readPolicy);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(datastoreConfig);	

		Query q = new Query("User");
		q.setFilter(new FilterPredicate("username", FilterOperator.EQUAL, "admin"));
		PreparedQuery pq = datastore.prepare(q);
		
		FetchOptions fetchOptions = FetchOptions.Builder.withLimit(1);
		if(pq.countEntities(fetchOptions) == 0) {
			User user = new User();
			user.setUsername("admin");
			user.setPassword("admin");
			System.out.println("Adding user");
		} else {
			System.out.println("User exists");
		}
		return "complete";
	}
	
	@RequestMapping(value="/init-user", method=RequestMethod.GET)
	public String setUser(ModelMap modelMap) {
		PersistenceManager pmf = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pmf.newQuery();
		q.setFilter("username == admin");
		User user = (User) q.execute();
		if(user == null) {
			System.out.println("User not found");
		} else {
			System.out.println("User found");
		}
		modelMap.addAttribute(user);
		return "complete";
	}
	

}
