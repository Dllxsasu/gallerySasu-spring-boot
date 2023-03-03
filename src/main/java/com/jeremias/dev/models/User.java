package com.jeremias.dev.models;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "Users")
@Builder
public class User  {
	
    private String id;

    @NotBlank(message = "username es requerido ")
    private String username;

    @Size(max = 120)
    @NotBlank(message = "Password es requerido ")
    private String password;
    @Email
    @NotEmpty(message = "Email es requerido")
    private String email;
    @DBRef
    private Set<Role> roles = new HashSet<>();
    private Instant created;

    private boolean enabled;

    
    private Set<String> likedFotos = new HashSet<>();
    private Set<String> historialVistas = new HashSet<>();

	public void addLikedFotos(String fotoId) {
        this.likedFotos.add(fotoId);
    }

    public void removeFromLikedFotos(String fotoId) {
    	this.likedFotos.remove(fotoId);
    }
    public void addHistorialFotos(String fotoId) {
    	historialVistas.add(fotoId);
    }

    
}
