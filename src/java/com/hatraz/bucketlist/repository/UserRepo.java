package com.hatraz.bucketlist.repository;

import java.util.List;

import javax.jdo.PersistenceManager;

import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.service.PMF;

public class UserRepo {
	public static User findByUserByTwitterId(long twitter_id) {
		javax.jdo.Query q = PMF.get().getPersistenceManager().newQuery(User.class);
		q.setFilter("twitter_id == twitter_id");
		
		List<User> results = (List<User>) q.execute(twitter_id);
		q.closeAll();
		
		if(results.size() == 0) {
			return null;
		}
		
		User user = (User) results.get(0);
		return user;
	}
	
	public static void save(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(user);
	}
}
