package com.hatraz.bucketlist.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import twitter4j.Twitter;
import twitter4j.TwitterException;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.service.ItemServiceImpl;
import com.hatraz.bucketlist.service.UserServiceImpl;

@Controller
@SessionAttributes({"UserID", "twitter", "requestToken"})
public class HomeController {
	private @Autowired HttpServletRequest request;
	private @Autowired UserServiceImpl userServiceImpl;
	private @Autowired ItemServiceImpl itemServiceImpl;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getHome(ModelMap model) {	
		return "home";
	}
	
	@RequestMapping(value="/get-favorites", method=RequestMethod.GET)
	public String getItems() {
		User user = userServiceImpl.findById((Integer) request.getSession().getAttribute("user_id"));
		System.out.print(user);
		Collection<Item> items = user.getFavoriteItems();

		System.out.print(items.size());
		for(Iterator<Item> i = items.iterator(); i.hasNext();) {
			Item item = i.next();
			System.out.println(item.getName());
		}
		return "home";
	}
	
	@RequestMapping(value="/set-favorites", method=RequestMethod.GET)
	public String setItem() throws Exception {
		User user = userServiceImpl.findById((Integer) request.getSession().getAttribute("user_id"));

		Item item = itemServiceImpl.findById(1);

		HashSet<Item> item_set = new HashSet<Item>();
		item_set.add(item);
		
		user.setFavoriteItems(item_set);
		user.setFirstName("Bob");
		userServiceImpl.update(user);
		return "home";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String getRegistration(ModelMap model) throws IllegalStateException, TwitterException {
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
		model.addAttribute("twitter_id", twitter.getId());
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public ModelAndView getRegistrationSubmit(ModelMap model, @RequestParam(value="username", required=true) String username, @RequestParam(value="password", required=true) String password) throws IllegalStateException, TwitterException, IOException {
		User user = new User();
		user.setFirstName("Test User");
		Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");

		user.setTwitterId(twitter.getId());
		user.setUsername(username);
		user.setPassword(password);

		userServiceImpl.create(user);
		return new ModelAndView("redirect:/");
	}

/*	@RequestMapping(value="/create-user", method=RequestMethod.GET)
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
	}*/
	

}
