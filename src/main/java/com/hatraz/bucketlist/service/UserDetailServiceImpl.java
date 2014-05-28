package com.hatraz.bucketlist.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import com.hatraz.bucketlist.model.Item;
import com.hatraz.bucketlist.model.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService, InitializingBean {
	
	@Autowired
	private UserServiceImpl userService;

	public UserDetails loadUserByUsername(String username)
	{
		System.out.println("Load by username");
		try {
			/**
			 * Not implemented
			 */
			return new User();
		} catch(Exception e) {
			System.out.println(e.toString());
			return new User();
		}
	}
	
	public UserDetails loadUserByTwitterId(Long id) {
		System.out.println("Load by Twitter ID");
		System.out.println(id);
		User user;
		user = userService.findByTwitterId(id);
		return user;
	}
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}
	
	/**
	 * Converts a numerical role to an equivalent list of roles
	 * @param role the numerical role
	 * @return list of roles as as a list of {@link String}
	 */
	public List<String> getRoles(Integer role) {
		List<String> roles = new ArrayList<String>();
		
		if (role.intValue() == 1) {
			roles.add("ROLE_USER");
			roles.add("ROLE_ADMIN");
			
		} else if (role.intValue() == 2) {
			roles.add("ROLE_USER");
		}
		
		return roles;
	}
	
	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		System.out.println("GrantedAuthorities");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
}
