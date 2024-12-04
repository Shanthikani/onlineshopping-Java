package com.example.shopping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.UserRepository;
import com.example.shopping.entity.User;
import com.example.shopping.security.CustomUserBean;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User with username "+username+"not found"));
		return CustomUserBean.createInstance(user);
	}
	
	

}
