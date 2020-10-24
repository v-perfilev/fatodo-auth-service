package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.ResetPasswordNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import com.persoff68.fatodo.web.rest.vm.ForgotPasswordVM;
import com.persoff68.fatodo.web.rest.vm.ResetPasswordVM;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final ActivationRepository activationRepository;
    private final ResetPasswordRepository resetPasswordRepository;
    private final LocalUserDetailsService localUserDetailsService;
    private final UserServiceClient userServiceClient;
    private final MailServiceClient mailServiceClient;
    private final PasswordEncoder passwordEncoder;

    public void activate(UUID code) {
        Activation activation = activationRepository.findByCode(code)
                .orElseThrow(ModelNotFoundException::new);
        if (activation.isCompleted()) {
            throw new UserAlreadyActivatedException();
        }
        userServiceClient.activate(activation.getUserId());
        activation.setCompleted(true);
        activationRepository.save(activation);
    }

    public void sendActivationCodeMail(String emailOrUsername) {
        UserPrincipal userPrincipal = localUserDetailsService.getUserPrincipalByEmailOrUserName(emailOrUsername);
        if (userPrincipal.isActivated()) {
            throw new UserAlreadyActivatedException();
        }
        sendActivationCodeMail(userPrincipal);
    }

    public void sendActivationCodeMail(UserPrincipal userPrincipal) {
        UUID activationCode = getActivationCode(userPrincipal.getId());
        ActivationMailDTO activationMailDTO = new ActivationMailDTO(userPrincipal, activationCode);
        mailServiceClient.sendActivationCode(activationMailDTO);
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

    public void sendResetPasswordMail(ForgotPasswordVM forgotPasswordVM) {
        UserPrincipal userPrincipal = localUserDetailsService
                .getUserPrincipalByEmailOrUserName(forgotPasswordVM.getUser());
        String resetPasswordCode = getResetPasswordCode(userPrincipal.getId());
        ResetPasswordMailDTO resetPasswordMailDTO = new ResetPasswordMailDTO(userPrincipal, resetPasswordCode);
        mailServiceClient.sendResetPasswordCode(resetPasswordMailDTO);
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

    private String getResetPasswordCode(UUID userId) {
        resetPasswordRepository.findByUserIdAndCompleted(userId, false)
                .ifPresent(resetPassword -> {
                    resetPassword.setCompleted(true);
                    resetPasswordRepository.save(resetPassword);
                });
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUserId(userId);
        resetPassword.setCode(UUID.randomUUID().toString());
        resetPasswordRepository.save(resetPassword);
        return resetPassword.getCode();
    }

}
