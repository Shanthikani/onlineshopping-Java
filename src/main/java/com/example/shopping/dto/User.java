package com.example.shopping.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.shopping.entity.Role;
import com.example.shopping.entity.Roles;




public class User {
private Integer id;
private String username;
private String email;
private String password;
private String[] roles;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String[] getRoles() {
	return roles;
}
public void setRoles(String[] list) {
	this.roles = list;
}

public static User toDTO(com.example.shopping.entity.User user)
{
	User userDTO= new User();
	userDTO.setId(user.getId());
	userDTO.setUsername(user.getUsername());
	userDTO.setEmail(user.getEmail());
	userDTO.setPassword(user.getPassword());
	
	//userDTO.setRoles(user.getRoles().toArray(new String[0])); 
//	userDTO.setRoles(user.getRoles().stream()
//            .map(Object::toString)  // Convert elements to String
//            .toArray(String[]::new));
	String[] roleArray=new String[user.getRoles().size()];
	int index=0;
	for(Role  role:user.getRoles())
	{
		roleArray[index]=role.getRoleName().toString();
		if(roleArray[index]=="ROLE_ADMIN")
		{
			roleArray[index]="Admin";
		}
		else
		{
			roleArray[index]="User";
		}
		index++;
	}
	userDTO.setRoles(roleArray);
	return userDTO;

}
public static com.example.shopping.entity.User toEntity(User userDTO)
{
	com.example.shopping.entity.User user= new com.example.shopping.entity.User();
	user.setId(userDTO.getId());
	user.setUsername(userDTO.getUsername());
	user.setEmail(userDTO.getEmail());
	user.setPassword(userDTO.getPassword());
	List<Role> rolesDTO=Arrays.stream(userDTO.getRoles()).map(roleItem->new Role()).collect(Collectors.toList());
	user.setRoles(rolesDTO);
	return user;
}
}
