package com.jeremias.dev.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jeremias.dev.security.services.JwtTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	

	  private final JwtTokenProvider jwtService;
	  private final UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Obtemos el auth header
		


		final String userMail;	
		//Obtemos la clave token
	    String token = getJWTfromRequest(request);
	   System.out.println("asd");
				
		
		if(StringUtils.hasText(token) &&  SecurityContextHolder.getContext().getAuthentication() == null) {
			//Obtemos de los claims el  email
			userMail = jwtService.extractUsername(token);
				//buscamos 
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userMail);
			//validamos el token sea correcto
			if(jwtService.isTokenValid(token, userDetails)) {
				//aqui seteamos los datos del usuario y la autoridad que tiene el mismo
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			            userDetails,
			            null,
			            userDetails.getAuthorities()
			        );
				///setemaos los deteails del request
				authToken.setDetails(
			            new WebAuthenticationDetailsSource().buildDetails(request)
			        );
				//Guardamos en el spring security
				  SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		  filterChain.doFilter(request, response);
		
	}
	
	private String getJWTfromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
	
	
}
