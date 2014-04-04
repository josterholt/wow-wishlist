package com.hatraz.bucketlist.security;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.hatraz.bucketlist.model.User;

public class TwitterUserAuthentication implements Authentication {
    private final User principal;
    private final Object details;
    private boolean authenticated;

    public TwitterUserAuthentication(User principal, Object details) {
    	System.out.println("Setting TwitterUserAuthentication");
        this.principal = principal;
        this.details = details;
        authenticated = true;
    }

    public Collection<GrantedAuthority> getAuthorities() {
    	System.out.println("Getting authorities");
        return new HashSet<GrantedAuthority>(principal.getAuthorities());
    }

    public Object getCredentials() {
    	System.out.println("Get credentials");
        throw new UnsupportedOperationException();
    }

    public Object getDetails() {
        return details;
    }

    public Object getPrincipal() {
        return principal;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        authenticated = isAuthenticated;
    }

    public String getName() {
    	// @todo Should this be a username?
        return principal.getUsername();
    }

    @Override
    public String toString() {
    	System.out.println("Printing string");
        return "GaeUserAuthentication{" +
                "principal=" + principal +
                ", details=" + details +
                ", authenticated=" + authenticated +
                '}';
    }
}
