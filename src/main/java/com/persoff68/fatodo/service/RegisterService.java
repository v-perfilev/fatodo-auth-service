package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserServiceClient userServiceClient;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final UserMapper userMapper;

    public void register(LocalUserDTO localUserDTO) {
        String encodedPassword = passwordEncoder.encode(localUserDTO.getPassword());
        localUserDTO.setPassword(encodedPassword);
        UserPrincipalDTO userPrincipalDTO = userServiceClient.createLocalUser(localUserDTO);
        UserPrincipal userPrincipal = userMapper.userPrincipalDTOToUserPrincipal(userPrincipalDTO);
        accountService.sendActivationCode(userPrincipal);
    }

}
