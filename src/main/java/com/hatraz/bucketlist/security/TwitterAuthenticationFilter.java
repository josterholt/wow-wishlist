package com.hatraz.bucketlist.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.service.UserDetailServiceImpl;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

public class TwitterAuthenticationFilter extends GenericFilterBean {
	  private static final String REGISTRATION_URL = "/register.htm";
	  private AuthenticationDetailsSource ads = new WebAuthenticationDetailsSource();
	  private AuthenticationManager authenticationManager;
	  private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
	  
	  @Autowired
	  private UserDetailServiceImpl userDetailServiceImpl;

	  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		/**
		 * Check if user is already authenticated
		 * If not, check if user has started oAuth handshake
		 * and authorize if needed
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		HttpServletRequest req = (HttpServletRequest) request;
		
		if (authentication == null) {
			System.out.println("Authentication is null");
			
	        Twitter twitter = (Twitter) req.getSession().getAttribute("twitter");
	        if(twitter != null) {
		        RequestToken requestToken = (RequestToken) req.getSession().getAttribute("requestToken");
		        String verifier = request.getParameter("oauth_verifier");
		        
		        try {
		        	System.out.println(verifier);
		        	System.out.println(requestToken);
		            twitter.getOAuthAccessToken(requestToken, verifier);
		            			
		            User user = (User) userDetailServiceImpl.loadUserByTwitterId(twitter.getId());
		            
		            // If user is null, this is a new user
		            if(user == null) {
		            	((HttpServletResponse) response).sendRedirect("/register");
		            	return;
		            }
		            
		            // Remove request token to clear login cycle
		            req.getSession().removeAttribute("requestToken");
		            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, null);
		            //token.setDetails(ads.buildDetails(request));
		            authentication = authenticationManager.authenticate(token);
		            SecurityContextHolder.getContext().setAuthentication(authentication);
		            System.out.println("Auth set");
	            	req.getSession().setAttribute("user_id", user.getId());
		            ((HttpServletResponse) response).sendRedirect("/");
		            return;
		        } catch(AuthenticationException e) {
		        	System.out.println("Auth failure");
		        	System.out.println(e.getMessage());
		        } catch (TwitterException e) {
					// TODO Auto-generated catch block
		        	System.out.println("Auth exception");
					//e.printStackTrace();
				}
			} else if(req.getHeader("Authorization") != null) {
				String auth_token = req.getHeader("Authorization");
				System.out.println(req.getAuthType());
				System.out.println(auth_token);
		        
	        } else {
	        	
	        	((HttpServletResponse) response).sendError(401);
		    	return;
	        }
		} else {
        	System.out.println("Authentication is not null");
		}
    
	    System.out.println("Continue down chain");
	    chain.doFilter(request, response);
	  }
	  
	  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
	    this.authenticationManager = authenticationManager;
	  }

	  public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
	    this.failureHandler = failureHandler;
	  }
}