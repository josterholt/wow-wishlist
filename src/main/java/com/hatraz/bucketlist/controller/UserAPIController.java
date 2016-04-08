package com.hatraz.bucketlist.controller;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
}
