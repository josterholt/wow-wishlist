package com.hatraz.bucketlist.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;


import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.repository.UserRepo;
import com.hatraz.bucketlist.service.UserServiceImpl;

public class InternalAuthenticationProvider implements AuthenticationProvider {
	@Autowired private UserServiceImpl userServiceImpl;
	@Autowired UserRepo userService;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	System.out.println("Running internal authentication");
    	
    	// Don't think this is casting to the right object
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userService.findByUsername(username);
        if(user != null) {
	        System.out.println("getPassword()");
	        System.out.println(user.getPassword());
	        System.out.println("Password");
	        System.out.println(password);;
	        if(!user.getPassword().equals(password)) {
	        	throw new BadCredentialsException("Bad Credentials");
	        }
        
	        //user.clearPassword();
	        user.password = "";
	        //List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
	        System.out.println("Return Authentication Token");
	        //authentication.getCredentials()
	        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        System.out.println("Returning null");
        return null;
    }

    public final boolean supports(Class<?> authentication) {
    	return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
