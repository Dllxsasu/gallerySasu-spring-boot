package com.jeremias.dev.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthentificationEntryPoint implements AuthenticationEntryPoint {
	///we override a commence from authentifaciton entryPoint
	//Esta funcion es llamada cuando entra alguien sin token o no es correcto el mismo
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		
		// TODO Auto-generated method stub
		
	}

}
