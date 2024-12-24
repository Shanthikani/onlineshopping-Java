package com.example.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping.dao.UserRepository;
import com.example.shopping.dto.User;
import com.example.shopping.security.CustomUserBean;
import com.example.shopping.service.SpringJPAUserService;


@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/user")
public class UserController {
	
	@Autowired private SpringJPAUserService userService;
	
	@GetMapping(value="/info")
	public ResponseEntity<User> getUserDetails()
	{
//		String username=SecurityContextHolder.getContext().getAuthentication().getPrincipal() (The issue here is getPrincipal() 
//		returning an instance of user object(CustomerUserBean) rather than the simple string, 
//		for that issue use the following sentence)
		String username=((CustomUserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		System.out.println(username);
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(username));
	}
	
	@PutMapping(value="/updateUser")
	public ResponseEntity<User> updateUserDetail(@RequestBody User user)
	{
		return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
	}
	@GetMapping("/allusers")
	public String displayUsers() {
		return "Display All Users";
	}
	
	@GetMapping("/displayuser")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public String displayToUser() {
		return "Display to both user and admin";
	}
	
	@GetMapping("/displayadmin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String displayToAdmin() {
		return "Display only to admin";
	}
}