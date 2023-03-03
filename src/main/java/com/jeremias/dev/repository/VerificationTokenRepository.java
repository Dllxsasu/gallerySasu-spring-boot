package com.jeremias.dev.repository;

import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.jeremias.dev.models.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
	Optional<VerificationToken> findByToken(String token);
}
