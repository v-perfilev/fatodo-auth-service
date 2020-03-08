package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserServiceClient userServiceClient;

    public void register(LocalUserDTO localUserDTO) {
        userServiceClient.createLocalUser(localUserDTO);
    }

}
