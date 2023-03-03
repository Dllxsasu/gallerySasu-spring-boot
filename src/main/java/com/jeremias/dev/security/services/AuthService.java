package com.jeremias.dev.security.services;

import static java.time.Instant.now;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeremias.dev.dtos.AuthenticationResponse;
import com.jeremias.dev.dtos.LoginRequest;
import com.jeremias.dev.dtos.MessageResponse;
import com.jeremias.dev.dtos.RefreshTokenRequest;
import com.jeremias.dev.dtos.RegisterRequest;
import com.jeremias.dev.exception.GalleryPhotosException;
import com.jeremias.dev.mail.MailContentBuilder;
import com.jeremias.dev.mail.MailService;
import com.jeremias.dev.models.ERole;
import com.jeremias.dev.models.NotificationEmail;
import com.jeremias.dev.models.Role;
import com.jeremias.dev.models.User;
import com.jeremias.dev.models.VerificationToken;
import com.jeremias.dev.repository.RoleRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.repository.VerificationTokenRepository;
import com.jeremias.dev.util.Constants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
	
	 	private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final VerificationTokenRepository verificationTokenRepository;
	    private final MailContentBuilder mailContentBuilder;
	    private final MailService mailService;
	    private final AuthenticationManager authenticationManager;
	    private final JwtTokenProvider jwtProvider;
	    private final RefreshTokenService refreshTokenService;
	    private final RoleRepository roleRepository;
	    private final UserDetailsServiceImpl  userDetailsService;
	    @Transactional
	    public ResponseEntity<?> signup(RegisterRequest registerRequest) {
	        //Se registra todo
	    	
	    	if (userRepository.existsByUsername(registerRequest.getUsername())) {	    		
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
			}
			if (userRepository.existsByEmail(registerRequest.getEmail())) {
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already in use!"));
			}
			
	    	User user = new User();
	        user.setEmail(registerRequest.getEmail());
	        user.setPassword(encodePassword(registerRequest.getPassword()));
	        user.setCreated(now());
	        user.setEnabled(false);
	        user.setUsername(registerRequest.getUsername());
	      //  Set<String> strRoles = signUpRequest.getRoles();
	        
			Set<Role> roles = new HashSet<>();
			
			Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
			
			if(userRole.isEmpty()) {
				Role rolex = new Role();
				rolex.setName(ERole.ROLE_USER); 
				roles.add(rolex);
			}else {	
			roles.add(userRole.get());
			}
			/*
			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					case "mod":
						Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}
				});
			}
	*/		
	        userRepository.save(user);
	        
	        //Generamos el token con los datyos
	        String token = generateVerificationToken(user);
	        String message = mailContentBuilder.build("Gracias por registrarte en SASUGallery, por haga click en el link para activar su cuenta : "
	                + Constants.ACTIVATION_EMAIL + "/" + token);

	        mailService.sendMail(new NotificationEmail("Active su cuenta en SasuGallery", user.getEmail(), message));
	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	    }
	    
	    public AuthenticationResponse login(LoginRequest loginRequest) {
	    	 
	  		 
	  		System.out.println("Entramos al login aqui");
	    	//Verificamos que este registrado con el authenticate caso contrario lanza un error
	    	  Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
	                loginRequest.getPassword()));
	    		System.out.println("Luego estamos aqui al login aqui");
	        //Seteamos en el context security
	        SecurityContextHolder.getContext().setAuthentication(authenticate);
	        
	        String token = jwtProvider.generateToken((UserDetails) authenticate.getPrincipal());
	        //builder responseAuth
	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExperationMs()))
	                .username(loginRequest.getUsername())
	                .build();
	    }
	    //xk vamos a realizar una consulta a bd o una operacion
	    @Transactional( )
	    public User getCurrentUser() {
	        User principal = (User) SecurityContextHolder.
	                getContext().getAuthentication().getPrincipal();
	        
	        return userRepository.findByUsername(principal.getUsername())
	                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado - " + principal.getUsername()));
	    }	
	    
	    private String generateVerificationToken(User user) {
	    	//generamos el token para validar el usuario
	        String token = UUID.randomUUID().toString();
	        VerificationToken verificationToken = new VerificationToken();
	        verificationToken.setToken(token);
	        verificationToken.setUserId(user.getId());
	        verificationTokenRepository.save(verificationToken);
	        return token;
	    }


	    private String encodePassword(String password) {
	        return passwordEncoder.encode(password);
	    }
	    
	    public void verifyAccount(String token) {
	    	//Buscamos el toekn caso contrario que salga una toek invalido
	        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
	        verificationTokenOptional.orElseThrow(() -> new GalleryPhotosException("Token invalidado"));
	        fetchUserAndEnable(verificationTokenOptional.get());
	    }
	    //porque realizamos una operacion ponemos la anotacion transactionla
	    @Transactional
	    private void fetchUserAndEnable(VerificationToken verificationToken) {
	    	//obtemos el username y lo setamos como habilitado
	        String userId = verificationToken.getUserId();
	        User user = userRepository.findById(userId).orElseThrow(() -> new GalleryPhotosException("User Not Found with id - " + userId));
	        user.setEnabled(true);
	        userRepository.save(user);
	    }
	    
	    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
	        //validamos que exista en la bd el refreshToken
	    	refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
	        //Buscamos el token
	    	User user = userRepository.findByUsername(refreshTokenRequest.getUsername())
					.orElseThrow();
	    	//Generamos el toekn
	    	UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
	        String token = jwtProvider.generateToken(userDetails);
	        //retornamos el repsonse with new token
	        return AuthenticationResponse.builder()
	                .authenticationToken(token)
	                .refreshToken(refreshTokenRequest.getRefreshToken())
	                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExperationMs()))
	                .username(refreshTokenRequest.getUsername())
	                .build();
	    }
}
