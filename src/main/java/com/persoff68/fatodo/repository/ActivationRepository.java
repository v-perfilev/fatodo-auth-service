package com.persoff68.fatodo.repository;

import com.persoff68.fatodo.model.Activation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationRepository extends MongoRepository<Activation, String> {

    Optional<Activation> findByCode(String code);

}
