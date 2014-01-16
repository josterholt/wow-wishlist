package com.hatraz.bucketlist.model;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class List {
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public Key getKey() {
		return key;
	}

	@Persistent
	private User user;
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User val) {
		user = val;
	}
	
	@Persistent	
	private String character;
	
	public String getCharacter() {
		return character;
	}
	
	public void setCharacter(String val) {
		character = val;
	}

	@Persistent
	private ArrayList<Item> items;
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public void setItems(ArrayList<Item> val) {
		items = val;
	}
	
	public void addItem(Item val) {
		items.add(val);
	}
	
}
