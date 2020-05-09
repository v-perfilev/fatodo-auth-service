package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.dto.ActivationDTO;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final ActivationService activationService;
    private final MailServiceClient mailServiceClient;

    public void register(LocalUserDTO localUserDTO) {
        String encodedPassword = passwordEncoder.encode(localUserDTO.getPassword());
        localUserDTO.setPassword(encodedPassword);
        UserDTO userDTO = userServiceClient.createLocalUser(localUserDTO);
        String activationCode = activationService.addActivation(userDTO.getId());
        ActivationDTO activationDTO = new ActivationDTO(userDTO, activationCode);
        mailServiceClient.activate(activationDTO);
    }

}
