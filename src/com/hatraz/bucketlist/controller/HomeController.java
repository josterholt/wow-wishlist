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

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;


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

	@RequestMapping(value="/twitter-login", method=RequestMethod.GET)
	public String twitterLogin(ModelMap model) {
		Twitter twitter = new TwitterFactory().getInstance();
		model.addAttribute("twitter", twitter);
		try {
			String callbackURL = "http://localhost:8080/twitter_callback";
			RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL);
			model.addAttribute("requestToken", requestToken);
			System.out.println(requestToken.getAuthenticationURL());
			return "redirect:" + requestToken.getAuthenticationURL();		
		} catch(TwitterException e) {
			return "complete";
		}		
	}
	
	@RequestMapping(value="/spring_security_login", method=RequestMethod.GET)
	public String handleFailure() {
		return "complete";
	}

	@RequestMapping(value="/twitter_callback", method=RequestMethod.GET)
	public String twitterCallback(ModelMap model, @RequestParam String oauth_verifier, HttpServletRequest request) {
		Twitter twitter = (Twitter) model.get("twitter");
		RequestToken requestToken = (RequestToken) model.get("requestToken");
		System.out.println(oauth_verifier);
		try {
			twitter.getOAuthAccessToken(requestToken, oauth_verifier);
			System.out.println(twitter.getId());
			model.remove("requestToken");
			
			// Check for existing user
			User user;
			user = UserRepo.findByUserByTwitterId(twitter.getId());
			if(user == null) {
				user = new User();
				user.setTwitterId(twitter.getId());
				UserRepo.save(user);
			}
			model.addAttribute("user", user);
			try {
		        // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
		        UserDetailServiceImpl udService = new UserDetailServiceImpl();
		        CustomMd5PasswordEncoder passEncoder = new CustomMd5PasswordEncoder();
		        ReflectionSaltSource saltSource = new ReflectionSaltSource();
		        saltSource.setUserPropertyToUse("salt");
		        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		        token.setDetails(new WebAuthenticationDetails(request));
		        DaoAuthenticationProvider authenticator = new DaoAuthenticationProvider();
		        authenticator.setUserDetailsService(udService);
		        authenticator.setPasswordEncoder(passEncoder);
		        authenticator.setSaltSource(saltSource);
		        Authentication authentication = authenticator.authenticate(token);
		        SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				e.printStackTrace();
				SecurityContextHolder.getContext().setAuthentication(null);
			}
		} catch(TwitterException e) {
			System.out.println("Exception with twitter");
			return "complete";
		}
		System.out.println("Done");
		return "complete";
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
