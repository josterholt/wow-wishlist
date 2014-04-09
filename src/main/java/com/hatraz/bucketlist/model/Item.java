package com.hatraz.bucketlist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="items")
public class Item {
	@Id
	@Column
	private Integer id;
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer val) {
		this.id = val;
	}
	
	@Column
	Integer wowId;
	
	public Integer getWoWId() {
		return this.wowId;
	}
	
	public void setWoWId(Integer wowId) {
		this.wowId = wowId;
	}
	
	@Column
	public String name;
	
	@Column
	public String summary;
	
	@Column
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
