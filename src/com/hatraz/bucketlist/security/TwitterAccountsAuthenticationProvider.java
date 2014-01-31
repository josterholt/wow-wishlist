package com.hatraz.bucketlist.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.hatraz.bucketlist.model.User;

public class TwitterAccountsAuthenticationProvider implements AuthenticationProvider {
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
/*    	System.out.println("Running authentication");
    	
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
        }

        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }

        return new TwitterUserAuthentication(user, authentication.getDetails());*/
    	return null;
    }

    public final boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
        
    private User getUser(Long id) {
    	System.out.println("getUser by ID: " + id);
		return null;
    	
    }
}