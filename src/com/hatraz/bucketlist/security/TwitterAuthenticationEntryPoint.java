package com.hatraz.bucketlist.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class TwitterAuthenticationEntryPoint implements AuthenticationEntryPoint {
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    UserService userService = UserServiceFactory.getUserService();

    System.out.println(request.getRequestURI());
    String login_url = userService.createLoginURL(request.getRequestURI());
    System.out.println(login_url);
    response.sendRedirect(login_url);
  }
}