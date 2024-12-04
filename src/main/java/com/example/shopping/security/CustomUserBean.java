package com.example.shopping.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; /* Provides core user information */

import com.example.shopping.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
/*org.springframework.security.core.userdetails.User  (for more concrete implementation we can use this class, but here we are using our own implementation */


public class CustomUserBean implements UserDetails{
	private static final long serialVersionUID= -4709084843450077569L;
	
	private Integer id;
	private String username;
	private String email;
	
	@JsonIgnore  /*annotation is a powerful tool in the Jackson library used to control the serialization and deserialization of JSON data. This annotation allows you to specify which fields or methods should be ignored during these processes, making it particularly useful for excluding sensitive or irrelevant information.*/
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	CustomUserBean(Integer id,String username,String email,String password, Collection<? extends GrantedAuthority> authorities)
	{
		this.id=id;
		this.username=username;
		this.email=email;
		this.password=password;
		this.authorities=authorities;
	}
	
	public static CustomUserBean createInstance(User user)
	{
		List<GrantedAuthority> authorities=user.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
		return new CustomUserBean(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(),authorities);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	
	@Override
	public int hashCode()
	{
		return username.hashCode();
	}
	
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}
	
	@Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }
	  @Override
	  public boolean equals(Object rhs) {
	    if (rhs instanceof CustomUserBean) {
	      return username.equals(((CustomUserBean) rhs).username);
	    }
	    return false;
	  }


}
