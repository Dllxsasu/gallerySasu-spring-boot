package com.jeremias.dev.repository;

import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.jeremias.dev.models.RefreshToken;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String>{
	Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
