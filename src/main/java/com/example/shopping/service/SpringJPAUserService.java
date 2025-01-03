package com.example.shopping.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopping.dao.RoleRepository;
import com.example.shopping.dao.UserRepository;
import com.example.shopping.dto.User;
import com.example.shopping.entity.Role;
import com.example.shopping.entity.Roles;

import jakarta.transaction.Transactional;

@Service
public class SpringJPAUserService implements AppUserService {
	
	@Autowired
	UserRepository userDAO;
	
	@Autowired
	RoleRepository roleDAO;
	
	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		User userDTO=new User();
		com.example.shopping.entity.User userEntity = userDAO.findByUsername(username).orElse(null);
		if(userEntity!=null)
		{
			
			userDTO=User.toDTO(userEntity);
			System.out.println(userDTO.getEmail());
			
		}
		return userDTO ;
	}

	@Override
	public List<User> getUsers(String role) {
		// TODO Auto-generated method stub
		return userDAO.findAll().stream().map(User::toDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public User updateUser(User userDTO) {
		// TODO Auto-generated method stub
		com.example.shopping.entity.User userEntity=new com.example.shopping.entity.User();
		try
		{
		userEntity=userDAO.findByUsername(userDTO.getUsername()).orElse(null);
		userEntity.setUsername(userDTO.getUsername());
		userEntity.setPassword(userDTO.getPassword());
		List<Role> roleList=new ArrayList<>();
		for(String role: userDTO.getRoles()) {
		      switch(role) {
		        case "admin":
		          roleList.add(roleDAO.findByRoleName(Roles.ROLE_ADMIN).get());
		          break;
		        case "user":
		          roleList.add(roleDAO.findByRoleName(Roles.ROLE_USER).get());
		          break;  
		      }
		    }
		userEntity.setRoles(roleList);
		userEntity=userDAO.save(userEntity);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return User.toDTO(userEntity);
	}

	@Override
	@Transactional
	public User saveUser(User userDTO) {
		// TODO Auto-generated method stub
		com.example.shopping.entity.User userEntity=new com.example.shopping.entity.User();
		userEntity.setId(userDTO.getId());
		userEntity.setUsername(userDTO.getUsername());
		userEntity.setPassword(userDTO.getPassword());
		userEntity.setRoles(Arrays.stream(userDTO.getRoles()).map(roleItem->new Role()).collect(Collectors.toList()));
		userEntity=userDAO.save(userEntity);
		
		return User.toDTO(userEntity);
	}

	@Override
	@Transactional
	public String deleteCurrency(String username) {
		// TODO Auto-generated method stub
		com.example.shopping.entity.User user=userDAO.findByUsername(username).orElse(null);
		userDAO.delete(user);
		return "Success";
	}

}
