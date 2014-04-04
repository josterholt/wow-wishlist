package com.hatraz.bucketlist.service;

import com.hatraz.bucketlist.model.User;
import java.util.List;

public interface UserService {
	public User create(User user);
	public User delete(int id) throws Exception;
	public List<User> findAll();
	public User update(User user) throws Exception;
	public User findById(int id);
	public User findByTwitterId(Long id);
}
