package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.ResetPassword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetPasswordRepository extends MongoRepository<ResetPassword, UUID> {

    Optional<ResetPassword> findByCode(String code);

    Optional<ResetPassword> findByUserIdAndCompleted(UUID userId, boolean completed);

}
