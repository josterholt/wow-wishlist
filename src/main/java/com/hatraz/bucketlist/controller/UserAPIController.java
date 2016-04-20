package com.hatraz.bucketlist.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.repository.ItemRepo;
import com.hatraz.bucketlist.repository.UserRepo;

@Controller
public class UserAPIController {
	private @Autowired HttpServletRequest request;
	@Autowired UserRepo userService;
	@Autowired ItemRepo itemService;
	
	@RequestMapping(value="/auth/login", method=RequestMethod.POST)
	public @ResponseBody Boolean login() {
		// Given we've gotten here, authentication must have succeeded
		return true;
	}
	
	@RequestMapping(value="/api/favorites/", method=RequestMethod.GET)
	@Transactional
	public @ResponseBody Collection<Item> getFavorites() {
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer userID = (Integer) request.getSession().getAttribute("user_id");

		if(userID == null) {
			return null;
		}

		User user = userService.findOne(userID);
		
		if(request.getParameter("ids") != null) {
			List<Integer> ints = new ArrayList<Integer>();
			for(String s : request.getParameter("ids").split(",")) {
				ints.add(Integer.valueOf(s));
			}
			return userService.getFilteredFavoriteItems(user.getId(), ints);
		} else {
			ArrayList<Item> items = new ArrayList<Item>(user.getFavoriteItems());
			return items;
			//return items.subList(0,  Math.min(items.size(), 10));	
		}
	}
	
	private User getUser() {
		Integer userID = (Integer) request.getSession().getAttribute("user_id");

		if(userID == null) {
			return null;
		}

		return userService.findOne(userID);
	}
	
	@RequestMapping(value="/api/getFavoriteIDs", method=RequestMethod.GET)
	public @ResponseBody List<Integer> getFavoriteIDs() {

		User user = getUser();
		List<Integer> ints = new ArrayList<Integer>();
		if(request.getParameter("ids") != null) {
			for(String s : request.getParameter("ids").split(",")) {
				ints.add(Integer.valueOf(s));
			}
		}
		
		List<Integer> itemIDs = new ArrayList<Integer>();
		for(Item item : userService.getFilteredFavoriteItems(user.getId(), ints)) {
			itemIDs.add(item.getId());
		}
		return itemIDs;
	}
			
	
	@RequestMapping(value="/api/favorite/{id}", method=RequestMethod.GET)
	@Transactional
	public @ResponseBody Boolean saveFavorite(@PathVariable(value="id") Integer id) {
		Integer userID = (Integer) request.getSession().getAttribute("user_id");

		if(userID == null) {
			return false;
		}

		User user = userService.findOne(userID);

		System.out.println("Looking up id: " + id.toString());
		Item item = itemService.findOne(id);		
		user.addFavoriteItem(item);
		userService.save(user);

		return true;
		
	}
	
	@RequestMapping(value="/api/favorite/{id}/delete", method=RequestMethod.GET)
	@Transactional
	public @ResponseBody Boolean deleteFavorite(@PathVariable(value="id") Integer id) {
		Integer userID = (Integer) request.getSession().getAttribute("user_id");

		if(userID == null) {
			return false;
		}

		User user = userService.findOne(userID);

		System.out.println("Looking up id: " + id.toString());
		Item item = itemService.findOne(id);
		
		Set<Item> items = user.getFavoriteItems();
		items.remove(item);
		
		user.setFavoriteItems(items);
		userService.save(user);

		return true;
		
	}	
}
