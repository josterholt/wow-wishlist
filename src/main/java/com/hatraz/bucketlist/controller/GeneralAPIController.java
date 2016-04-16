package com.hatraz.bucketlist.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GeneralAPIController {
	@RequestMapping(value="/auth/ping", method=RequestMethod.GET)
	public ResponseEntity<String> ping() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/api/login", method=RequestMethod.POST)
	public ResponseEntity<String> login() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}	
}
