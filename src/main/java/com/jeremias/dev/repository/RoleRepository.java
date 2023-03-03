package com.jeremias.dev.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jeremias.dev.models.ERole;
import com.jeremias.dev.models.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
	 Optional<Role> findByName(ERole name);
}
