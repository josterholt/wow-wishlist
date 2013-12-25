package com.hatraz.bucketlist.service;

import User;
import UserRepository;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService, InitializingBean {
	
	//@Autowired private UserRepository userRepo;

	public UserDetails loadUserByUsername(String username)
	{
		/*
		try {
			User user = userRepo.findByUsername(username);
			if(user == null) {
				return new User();
			}
			return user;
		} catch(Exception e) {
			System.out.println(e.toString());
			return new User();
		}
		*/
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
