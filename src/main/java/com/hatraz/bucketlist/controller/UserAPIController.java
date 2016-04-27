package com.hatraz.bucketlist.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
	public @ResponseBody Map<String, Boolean> login() {
		// Given we've gotten here, authentication must have succeeded
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			return Collections.singletonMap("status", false);
		} else {
			return Collections.singletonMap("status", true);
		}
	}
	
	@RequestMapping(value="/api/favorites/", method=RequestMethod.GET)
	@Transactional
	public @ResponseBody Collection<Item> getFavorites() {
		User user = getUser();

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
		Authentication auth = ((SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT")).getAuthentication();
		User principal = null;
		if(auth != null) {
			principal = (User) auth.getPrincipal();
		}		
		return userService.findOne(principal.getId());
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
	public @ResponseBody Map<String, Boolean> saveFavorite(@PathVariable(value="id") Integer id) {
		User user = getUser();

		System.out.println("Looking up id: " + id.toString());
		Item item = itemService.findOne(id);		
		user.addFavoriteItem(item);
		userService.save(user);

		return Collections.singletonMap("success", true);
		
	}
	
	@RequestMapping(value="/api/favorite/{id}/delete", method=RequestMethod.GET)
	@Transactional
	public @ResponseBody Map<String, Boolean> deleteFavorite(@PathVariable(value="id") Integer id) {
		User user = getUser();
		
		Item item = itemService.findOne(id);
		
		Set<Item> items = user.getFavoriteItems();
		items.remove(item);
		
		user.setFavoriteItems(items);
		userService.save(user);

		return Collections.singletonMap("success", true);
		
	}	
}
