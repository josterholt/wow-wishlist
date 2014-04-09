package com.hatraz.bucketlist.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public User create(User user) {
		return userRepo.save(user);
	}

	@Override
	public User delete(int id) throws Exception {
		User user = userRepo.findOne(id);
		if(user == null) {
			throw new Exception("User does not exist");
		}
		userRepo.delete(user);
		return user;
	}

	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public User update(User user) throws Exception {
		User updatedUser = userRepo.findOne(user.getId());
		if(user == null) {
			throw new Exception("User not found");
		}

		updatedUser.setFirstName(user.getFirstName());
		updatedUser.setFavoriteItems(user.getFavoriteItems());
		this.userRepo.save(updatedUser);
		//updatedUser = userRepo.save(user);		
		return updatedUser;
	}

	@Override
	public User findById(int id) {
		return userRepo.findOne(id);
	}

	@Override
	public User findByTwitterId(Long id) {
		System.out.println("findByTwitterId (UserSerivceImpl)");
		return userRepo.findByTwitterId(id);
	}

}
