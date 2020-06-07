package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.ResetPassword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends MongoRepository<ResetPassword, String> {

    Optional<ResetPassword> findByUserIdAndCompleted(String code, boolean completed);

    Optional<ResetPassword> findByCode(String code);

}
