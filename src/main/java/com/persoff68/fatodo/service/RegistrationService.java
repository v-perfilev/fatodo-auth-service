package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;

    public void register(LocalUserDTO localUserDTO) {
        String encodedPassword = passwordEncoder.encode(localUserDTO.getPassword());
        localUserDTO.setPassword(encodedPassword);
        userServiceClient.createLocalUser(localUserDTO);
    }

}
