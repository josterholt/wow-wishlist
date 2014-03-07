package com.hatraz.bucketlist.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@PersistenceCapable
public class Item {
	@Id
	@Persistent
	private String id;
	
	@Persistent Integer wowId;
	
	public Integer getId() {
		return wowId;
	}
	
	public void setId(Integer val) {
		this.wowId = val;
	}
	
	
	@Persistent
	public String name;
	
	@Persistent
	public String summary;
	
	@Persistent
	private String description;
	
	public String getName() {
		return name;
	}
	
	public void setName(String val) {
		this.name = val;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String val) {
		this.summary = val;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String val) {
		this.description = val;
	}
}
