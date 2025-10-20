package ar.edu.iua.iw3.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.edu.iua.iw3.auth.User;

public class BaseRestController {
	protected User getUserLogged() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) auth.getPrincipal();
		return user;
	}

}
