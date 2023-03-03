package com.jeremias.dev.service;

import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.FotoDto;
import com.jeremias.dev.exception.GalleryPhotosException;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.UserRepository;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

    public void addFoto(FotoDto dto) {
        
    	var currentUser = getCurrentUser();
        currentUser.addHistorialFotos(dto.getFotoId());
        userRepository.save(currentUser);
    }



    public void addLikedFotos(String fotoId) {
        var user = getCurrentUser();
        user.addLikedFotos(fotoId);
        userRepository.save(user);
    }

    public void removeFromLikedVideos(String fotoId) {
    	var user = getCurrentUser();
        user.removeFromLikedFotos(fotoId);
        userRepository.save(user);
    }




    public boolean ifLikedVideo(String videoId) {
        return getCurrentUser().getLikedFotos().stream().anyMatch(id -> id.equals(videoId));
    }

    

    private User getCurrentUser() {
    	//Obtemos de la authentificaion el token
    	//SecuritContexColder =SpringContenedorSegurida obtemos el contexto actual, obtemos el Authentficate
    	//con el getPrincipal obtemos el objeto USER
    	//de aqui jalamos el sub donde se encuentra la clave tokens
        User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        //buscamos que se encuentre o caso contratio lanzamos un thowError, para informar que no se pudo encontrar el usuario
        return userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new GalleryPhotosException("No se pudo encontrar el usuario - " + user.getUsername()));
    }

    public Set<String> getLikedFotos(String userId) {
        //Obtemos a list<string> donde se encuentramos una lsita de videos
    	User user = userRepository.findById(userId).orElseThrow(() -> new GalleryPhotosException("User invalido - " + userId));
        return user.getLikedFotos();
    }

    
}
