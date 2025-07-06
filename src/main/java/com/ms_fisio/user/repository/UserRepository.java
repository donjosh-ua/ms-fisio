package com.ms_fisio.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.user.domain.model.UserModel;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
