package com.hatraz.bucketlist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.service.UserServiceImpl;

public class TwitterAccountsAuthenticationProvider implements AuthenticationProvider {
	@Autowired private UserServiceImpl userServiceImpl;
	
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	System.out.println("Running authentication");
    	
    	// Don't think this is casting to the right object
        Object twitterUser = authentication.getPrincipal();
        System.out.println(twitterUser);

        User user = this.getUser(((User) twitterUser).getTwitterId());

        if (user == null) {
        	System.out.println("User does not exist, creating new one");
            // User not in registry. Needs to register
            user = new User();
            user.setTwitterId(((User) twitterUser).getTwitterId());
            //Save user?
            System.out.println("User doesn't exist");
            return null;
        }

        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }

        return new TwitterUserAuthentication(user, authentication.getDetails());
    }

    public final boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
        
    private User getUser(Long id) {
    	return userServiceImpl.findByTwitterId(id);    	
    }
}