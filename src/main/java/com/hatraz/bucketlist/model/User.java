package com.hatraz.bucketlist.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "users")
public class User implements UserDetails {
	@Column
	@Id
	private int id;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Column
	private String username;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String val) {
		username = val;
	}

	@Column
	public String password;
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String val) {
		password = val;
	}
	
	@Column
	private String first_name;
	
	public String getFirstName() {
		return first_name;
	}
	
	public void setFirstName(String val) {
		first_name = val;
	}
	
	@Column
	private String last_name;
	
	@Transient
	private Roles[] roles;
	
	public String last_name() {
		return last_name;
	}
	
	public void last_name(String val) {
		last_name = val;
	}
	
	@Column
	private long twitter_id;
	
	public long getTwitterId() {
		return twitter_id;
	}
	
	public void setTwitterId(long val) {
		twitter_id = val;
	}
	
	@ManyToMany(
		targetEntity=com.hatraz.bucketlist.model.Item.class, 
		cascade={CascadeType.PERSIST, CascadeType.MERGE}
	)
	@JoinTable(
		name="user_item_favorites",
		joinColumns={@JoinColumn(name="user_id")},
		inverseJoinColumns={@JoinColumn(name="item_id")}
	)
	private Set<Item> favorite_items;
	
	public Set<Item> getFavoriteItems() {
		return this.favorite_items;
	}
	
	public void setFavoriteItems(Set<Item> items) {
		this.favorite_items = items;
	}

	public void addFavoriteItem(Item favorite_item) {
		this.favorite_items.add(favorite_item);
	}
	
	public void removeFavoriteItem(Item favorite_item) {
		this.favorite_items.remove(favorite_item);
	}
	
	public enum Roles implements GrantedAuthority {
		ROLE_USER;

		public String getAuthority() {
			return name();
		}
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.<GrantedAuthority>asList(Roles.ROLE_USER);
	}

	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
