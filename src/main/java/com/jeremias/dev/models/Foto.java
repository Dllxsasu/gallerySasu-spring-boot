package com.jeremias.dev.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jeremias.dev.constantes.PhotoStatus;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Fotos")
public class Foto {
	private String id;
	private String userId;
	private String url;
	private String titulo;
	private AtomicInteger likes;

    private List<String> tags;
    private AtomicInteger Views;
    private PhotoStatus status;
    private Set<String> likedVideos = new HashSet<>();
    private Set<String> historialVistas = new LinkedHashSet<>();
    private Instant fechaSub;
    public int likeCount() {
        return likes.get();
    }



    public void addFotoHistorial(String fotoId) {
    	historialVistas.add(fotoId);
    }
    public void increaseViewCount() {
    	Views.incrementAndGet();
    }

    public void increaseLikeCount() {
        likes.incrementAndGet();
    }

    public void decreaseLikeCount() {
        likes.decrementAndGet();
    }

 
   
}
