package com.hatraz.bucketlist.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.hatraz.bucketlist.model.User;

public class TwitterAccountsAuthenticationProvider implements AuthenticationProvider {
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	// Don't think this is casting to the right object
        User twitterUser = (User) authentication.getPrincipal();

        User user = this.getUser(twitterUser.getTwitterId());

        if (user == null) {
            // User not in registry. Needs to register
            user = new User();
            user.setTwitterId(twitterUser.getTwitterId());
            //Save user?
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
		return null;
    	
    }
}