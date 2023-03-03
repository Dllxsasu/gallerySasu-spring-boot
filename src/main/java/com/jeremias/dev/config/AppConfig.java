package com.jeremias.dev.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jeremias.dev.repository.UserRepository;

import lombok.AllArgsConstructor;
@Configuration
@AllArgsConstructor
public class AppConfig {
	 private final UserRepository repository; 
/*
	 @Bean
	  public UserDetailsService userDetailsService() {
	    return username -> repository.findByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	  }
	*/
	@Bean
	  public ModelMapper ModelMapper() {
		  return new ModelMapper();
	  }
	  
}
