package com.example.shopping.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping.dao.RoleRepository;
import com.example.shopping.dao.UserRepository;
import com.example.shopping.entity.Role;
import com.example.shopping.entity.Roles;
import com.example.shopping.entity.User;
import com.example.shopping.security.AuthResponse;
import com.example.shopping.security.CustomUserBean;
import com.example.shopping.security.JwtTokenUtil;

@RestController
@CrossOrigin(origins="http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	 @PostMapping(value="/login",consumes = "application/json")
	  public ResponseEntity<?> userLogin(@RequestBody User user) throws AuthenticationException {
	   System.out.println("Before Authentication");
	   try {
	    Authentication authentication = authenticationManager.authenticate(
	          new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	    System.out.println("After Authentication");
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	    String token = jwtTokenUtil.generateJwtToken(authentication);
	    CustomUserBean userBean = (CustomUserBean) authentication.getPrincipal(); 
	    
	    List<String> roles = userBean.getAuthorities().stream()
	                   .map(auth -> auth.getAuthority())
	                   .collect(Collectors.toList());
	    AuthResponse authResponse = new AuthResponse();
	    authResponse.setToken(token);
	    authResponse.setRoles(roles);
	    authResponse.setUsername(user.getUsername());
	    System.out.println(authResponse.getToken());
	    return ResponseEntity.ok(authResponse);
	  }
	   catch(BadCredentialsException e)
	   {
		  e.printStackTrace();
	   }
	   return ResponseEntity.badRequest().body("Bad Credentials");
	 }
	 
	 
	  @PostMapping(value="/signup",consumes = "application/json")
	  public ResponseEntity<?> userSignup( @RequestBody com.example.shopping.dto.User signupRequest) {
		  System.out.println("Hit the PostMapping");
	    if(userRepository.existsByUsername(signupRequest.getUsername())){
	    	System.out.println("Checked ExistsByUsername");
	      return ResponseEntity.badRequest().body("Username is already taken");
	    }
	    if(userRepository.existsByEmail(signupRequest.getEmail())){
	    	System.out.println("Checked ExistsByEmail");
	      return ResponseEntity.badRequest().body("Email is already taken");
	    }
	    User user = new User();
	    List<Role> roles = new ArrayList<>();
	    user.setUsername(signupRequest.getUsername());
	    System.out.println(user.getUsername());
	    user.setEmail(signupRequest.getEmail());
	    user.setPassword(encoder.encode(signupRequest.getPassword()));
	    //System.out.println("Encoded password--- " + user.getPassword());
	    String[] roleArr = signupRequest.getRoles();
	    
	    if(roleArr == null) {
	      roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
	    }
	    for(String role: roleArr) {
	      switch(role) {
	        case "admin":
	          roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
	          break;
	        case "user":
	          roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
	          break;  
	        default:
	          return ResponseEntity.badRequest().body("Specified role not found");
	      }
	    }
	    user.setRoles(roles);
	    userRepository.save(user);
	    return ResponseEntity.ok("User signed up successfully");
	  }

}
