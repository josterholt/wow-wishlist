package com.hatraz.bucketlist.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hatraz.bucketlist.model.User;
import com.hatraz.bucketlist.service.UserDetailServiceImpl;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

@Controller
public class LoginController {
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginForm() {
		return "401";
	}
	
	@RequestMapping(value="/twitter-login", method=RequestMethod.GET)
	public void twitterLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = new TwitterFactory().getInstance();
        request.getSession().setAttribute("twitter", twitter);
        try {
            StringBuffer callbackURL = request.getRequestURL();

            
            int index = callbackURL.lastIndexOf("/");
            callbackURL.replace(index, callbackURL.length(), "").append("/api/twitter-callback");
            
            System.out.println(callbackURL);
            RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
            request.getSession().setAttribute("requestToken", requestToken);
            response.sendRedirect(requestToken.getAuthenticationURL());

        } catch (TwitterException e) {
            throw new ServletException(e);
        }
		return;
	}
	
	@RequestMapping(value="/twitter-callback", method=RequestMethod.GET)
	public void twitterCallback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Twitter callback");
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
            request.getSession().removeAttribute("requestToken");
            
  	      	User user = (User) UserDetailServiceImpl.loadUserByTwitterId(twitter.getId());
  	      	//SecurityContextHolder.getContext().getAuthentication();
  	      	// If no user, then this is a new user and they need an extra step
  	      	// If user, then redirect to new page
  	      	// Add user to security context
            
/*            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            authentication
            
*/
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
        response.sendRedirect(request.getContextPath() + "/");
		return;
	}
}
