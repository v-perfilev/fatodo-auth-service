package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.vm.ResetPasswordVM;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import com.persoff68.fatodo.service.client.EventService;
import com.persoff68.fatodo.service.client.MailService;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.ResetPasswordNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final LocalUserDetailsService localUserDetailsService;
    private final EventService eventService;
    private final MailService mailService;
    private final ActivationRepository activationRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;

    public void activate(UUID code) {
        Activation activation = activationRepository.findByCode(code)
                .orElseThrow(ModelNotFoundException::new);
        if (activation.isCompleted()) {
            throw new UserAlreadyActivatedException();
        }
        UUID userId = activation.getUserId();
        activation.setCompleted(true);
        activationRepository.save(activation);
        userServiceClient.activate(userId);
        eventService.sendWelcomeEvent(userId);
    }

    public void sendActivationCodeMail(String emailOrUsername) {
        UserPrincipal userPrincipal = localUserDetailsService.getUserPrincipalByUsernameOrEmail(emailOrUsername);
        if (userPrincipal.isActivated()) {
            throw new UserAlreadyActivatedException();
        }
        sendActivationCodeMail(userPrincipal);
    }

    public void sendActivationCodeMail(UserPrincipal userPrincipal) {
        UUID activationCode = getActivationCode(userPrincipal.getId());
        mailService.sendActivationCode(userPrincipal, activationCode);
    }

    public void resetPassword(ResetPasswordVM resetPasswordVM) {
        ResetPassword resetPassword = resetPasswordRepository.findByCode(resetPasswordVM.getCode())
                .orElseThrow(ResetPasswordNotFoundException::new);
        if (resetPassword.isCompleted()) {
            throw new ResetPasswordNotFoundException();
        }
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setUserId(resetPassword.getUserId());
        resetPasswordDTO.setPassword(passwordEncoder.encode(resetPasswordVM.getPassword()));
        userServiceClient.resetPassword(resetPasswordDTO);
        resetPassword.setCompleted(true);
        resetPasswordRepository.save(resetPassword);
    }

    public void sendResetPasswordMail(String user) {
        UserPrincipal userPrincipal = localUserDetailsService
                .getUserPrincipalByUsernameOrEmail(user);
        UUID resetPasswordCode = getResetPasswordCode(userPrincipal.getId());
        mailService.sendResetPasswordCode(userPrincipal, resetPasswordCode);
    }

    private UUID getActivationCode(UUID userId) {
        Activation activation = activationRepository.findByUserIdAndCompleted(userId, false)
                .orElse(null);
        if (activation == null) {
            activation = new Activation();
            activation.setUserId(userId);
        }
        activation.setCode(UUID.randomUUID());
        activationRepository.save(activation);
        return activation.getCode();
    }

    private UUID getResetPasswordCode(UUID userId) {
        resetPasswordRepository.findByUserIdAndCompleted(userId, false)
                .ifPresent(resetPassword -> {
                    resetPassword.setCompleted(true);
                    resetPasswordRepository.save(resetPassword);
                });
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUserId(userId);
        resetPassword.setCode(UUID.randomUUID());
        resetPasswordRepository.save(resetPassword);
        return resetPassword.getCode();
    }

}
