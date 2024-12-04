package com.example.shopping.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.shopping.exception.JwtAuthenticationEntryPoint;
import com.example.shopping.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled=true, jsr250Enabled=true)
public class SecurityConfig{
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
		authenticationManagerBuilder.userDetailsService(userDetailsService)
									.passwordEncoder(passwordEncoder());
	}
	
	@Bean 
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                           .userDetailsService(userDetailsService)
                           .passwordEncoder(passwordEncoder())
                           .and()
                           .build();
    }
	@Bean
	JwtTokenFilter jwtTokenFilter(){
		return new JwtTokenFilter();
	}
	
	@Bean
	
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/user/allusers").permitAll()
            .anyRequest().authenticated();

        // Add the JWT filter before the UsernamePasswordAuthenticationFilter
        http.addFilterAt(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build(); // Return the configured SecurityFilterChain
    }
	
}
