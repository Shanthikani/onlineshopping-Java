package com.example.shopping.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.shopping.entity.Role;
import com.example.shopping.entity.Roles;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	public Optional<Role> findByRoleName(Roles role);
}
