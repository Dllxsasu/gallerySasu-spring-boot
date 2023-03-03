package com.jeremias.dev.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.jeremias.dev.models.Foto;

public interface FotoRepository  extends MongoRepository<Foto, String>{
	 List<Foto> findByUserId(String userId);

	    List<Foto> findByTagsIn(List<String> tags);

	    List<Foto> findByIdIn(Set<String> likedVideos);
}
