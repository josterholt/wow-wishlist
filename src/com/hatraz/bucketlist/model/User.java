package com.hatraz.bucketlist.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class User {
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public Key getKey() {
		return key;
	}

	@Persistent
	private String first_name;
	
	public String getFirstName() {
		return first_name;
	}
	
	public void setFirstName(String val) {
		first_name = val;
	}
	
	@Persistent
	private String last_name;
	
	public String last_name() {
		return last_name;
	}
	
	public void last_name(String val) {
		last_name = val;
	}
}
