package com.paul.fyp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.paul.fyp.models.ERole;
import com.paul.fyp.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
