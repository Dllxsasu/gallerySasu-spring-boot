package com.jeremias.dev.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jeremias.dev.security.services.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private  final JwtAuthenticationFilter jwtAuthFilter;
	private JwtAuthentificationEntryPoint unauthorizedHandler;
	private final UserDetailsServiceImpl userDetailsService;
	
	private static final String[] AUTH_WHITE_LIST = {
			"/authenticate",
	        "/swagger-resources/**",
	        "/swagger-ui/**",
	        
	        "/v3/api-docs/**",
	        "/webjars/**",
	        "/api/auth/**"
    };
	@Bean
	  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		
		 
      
         http.cors().and().csrf().disable()
         .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
         .authorizeRequests().requestMatchers(AUTH_WHITE_LIST).permitAll()
         .requestMatchers("/api/test/**").permitAll()
         .anyRequest().authenticated();
     
     http.authenticationProvider(authenticationProvider());

     http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
     
	    return http.build();
	  }
	 
	 @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	 @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	  }
	 @Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	       
	      authProvider.setUserDetailsService(userDetailsService);
	      authProvider.setPasswordEncoder(passwordEncoder());
	   
	      return authProvider;
	  }

}
