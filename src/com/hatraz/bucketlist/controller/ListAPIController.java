package com.hatraz.bucketlist.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hatraz.bucketlist.model.Item;

@Controller
public class ListAPIController {
	@RequestMapping(value="/api/list/:list_id/item/{itemId}/add", method=RequestMethod.POST)	
	public String addItem(@PathVariable Integer itemId) {
		// Grab user from session
		// Pull list
		// Add item to list
		return null;	
	}

	@RequestMapping(value="/api/list/{listId}/item/{itemId}/delete", method=RequestMethod.POST)
	public String removeItem(@PathVariable Integer listId, @PathVariable Integer itemId) {
		// Grab user from session
		// Pull list
		// Remove item from list
		return null;
	}

	@RequestMapping(value="/api/list/{listId}", method=RequestMethod.POST)
	public ArrayList<Item> getList(@PathVariable Integer listId) {
		// Grab user from session
		// Pull and return list
		return null;
	}
}
