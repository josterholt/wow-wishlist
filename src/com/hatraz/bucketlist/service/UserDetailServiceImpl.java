package com.hatraz.bucketlist.service;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService, InitializingBean {
	
	//@Autowired private UserRepository userRepo;

	public UserDetails loadUserByUsername(String username)
	{
		System.out.println("Load by username");
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query q = pm.newQuery(User.class);
			User user = (User) q.execute();

			if(user == null) {
				return new User();
			}
			return user;
		} catch(Exception e) {
			System.out.println(e.toString());
			return new User();
		}
	}
	
	public static UserDetails loadUserByTwitterId(Long id) {
		System.out.println("Load by Twitter ID");
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query q = pm.newQuery(User.class);
			q.setFilter("TwitterID == twitter_id");
			User user = (User) q.execute(id);
			if(user == null) {
				return new User();
			}
			return user;
		} catch(Exception e) {
			System.out.println(e.toString());
			return new User();
		}
	}
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
