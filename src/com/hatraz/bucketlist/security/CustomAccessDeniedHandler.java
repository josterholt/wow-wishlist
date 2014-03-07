package com.hatraz.bucketlist.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	public void handle(HttpServletRequest arg0, HttpServletResponse arg1,
			AccessDeniedException arg2) throws IOException, ServletException {

		
	}
}
