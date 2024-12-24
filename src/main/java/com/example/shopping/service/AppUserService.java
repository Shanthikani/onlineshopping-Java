package com.example.shopping.service;

import java.util.List;

import com.example.shopping.dto.User;

public interface AppUserService {
	
	public User getUser(String username);
	public List<User> getUsers(String role);
	public User updateUser(User user);
	public User saveUser(User user);
	public String deleteCurrency(String username);
}
