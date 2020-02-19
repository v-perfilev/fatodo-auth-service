package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.constant.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserServiceClient userServiceClient;

    public void register(LocalUserDTO localUserDTO) {
        localUserDTO.setProvider(AuthProvider.LOCAL);
        userServiceClient.createLocalUser(localUserDTO);
    }

}
