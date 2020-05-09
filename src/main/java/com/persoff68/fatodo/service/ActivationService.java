package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationService {

    private final ActivationRepository activationRepository;
    private final UserServiceClient userServiceClient;

    public void activate(String code) {
        Activation activation = activationRepository.findByCode(code)
                .orElseThrow(ModelNotFoundException::new);
        if (!activation.isActivated()) {
            userServiceClient.activate(activation.getUserId());
            activation.setActivated(true);
            activationRepository.save(activation);
        } else {
            throw new UserAlreadyActivatedException();
        }
    }

    public String addActivation(String userId) {
        Activation activation = new Activation();
        activation.setUserId(userId);
        activation.setCode(UUID.randomUUID().toString());
        activationRepository.save(activation);
        return activation.getCode();
    }

}
