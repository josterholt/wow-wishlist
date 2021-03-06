package com.hatraz.bucketlist.security;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hatraz.bucketlist.model.User;



@SessionAttributes({"User"})
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		System.out.println("Setting authentication");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();
		if(user == null) {
			System.out.println("User is empty in success handler");
		} else {
			System.out.println("User ID:");
			System.out.println(user.getId());
		}
		response.setStatus(HttpServletResponse.SC_OK);
		clearAuthenticationAttributes(request);
	}
}
