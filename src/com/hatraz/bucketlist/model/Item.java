package com.hatraz.bucketlist.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;

@PersistenceCapable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public Key getKey() {
		return key;
	}
	
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
