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
	String icon;
	
	public String getIcon() {
		return this.icon;
	}
	
	public void setIcon(String val) {
		this.icon = val;
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
	private String name;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String value) {
		this.name = value;
	}
	
	@Column
	public String summary;
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String val) {
		this.summary = val;
	}	
	
	@Column
	private String description;
		
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String val) {
		this.description = val;
	}
	
	@Column
	private Integer requiredSkillRank;
	
	public Integer getRequiredSkillRank() {
		return this.requiredSkillRank;
	}
	
	public void setRequiredSkillRank(Integer value) {
		this.requiredSkillRank = value;
	}
	
	@Column
	private Integer itemLevel;
	
	public Integer getItemLevel() {
		return this.itemLevel;
	}
	
	public void setItemLevel(Integer value) {
		this.itemLevel = value;
	}
}
