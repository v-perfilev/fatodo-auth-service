package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.ActivationDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final ActivationRepository activationRepository;
    private final UserServiceClient userServiceClient;
    private final MailServiceClient mailServiceClient;

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

    public void resendActivationCode(String user) {

    }

    public void sendActivationCode(UserPrincipal userPrincipal) {
        String activationCode = getActivationCode(userPrincipal.getId());
        ActivationDTO activationDTO = new ActivationDTO(userPrincipal, activationCode);
        mailServiceClient.activate(activationDTO);
    }

    private String getActivationCode(String userId) {
        Optional<Activation> activationOptional = activationRepository.findByUserId(userId);
        Activation activation;
        if (activationOptional.isPresent()) {
            activation = activationOptional.get();
            if (activation.isActivated()) {
                throw new UserAlreadyActivatedException();
            }
        } else {
            activation = new Activation();
            activation.setUserId(userId);
            activation.setCode(UUID.randomUUID().toString());
            activationRepository.save(activation);
        }
        return activation.getCode();
    }

}
