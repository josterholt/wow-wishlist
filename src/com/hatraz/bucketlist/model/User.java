package com.hatraz.bucketlist.model;

import java.util.Arrays;
import java.util.Collection;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class User implements UserDetails {
	@Id
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public Key getKey() {
		return key;
	}
	
	@Persistent
	private String username;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String val) {
		username = val;
	}

	public String password;
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String val) {
		password = val;
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
	private Roles[] roles;
	
	public String last_name() {
		return last_name;
	}
	
	public void last_name(String val) {
		last_name = val;
	}
	
	@Persistent
	private long twitter_id;
	
	public long getTwitterId() {
		return twitter_id;
	}
	
	public void setTwitterId(long val) {
		twitter_id = val;
	}
	
	public enum Roles implements GrantedAuthority {
		ROLE_USER;

		public String getAuthority() {
			return name();
		}
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.<GrantedAuthority>asList();
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
