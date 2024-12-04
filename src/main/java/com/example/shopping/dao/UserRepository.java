package com.example.shopping.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public Optional<User> findByUsername(String username);
	
	public boolean existsByEmail(String email);
	public boolean existsByUsername(String username);
	

}
