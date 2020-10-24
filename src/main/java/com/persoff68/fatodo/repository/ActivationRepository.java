package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Activation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivationRepository extends MongoRepository<Activation, UUID> {

    Optional<Activation> findByCode(UUID code);

    Optional<Activation> findByUserIdAndCompleted(UUID userId, boolean completed);

}
