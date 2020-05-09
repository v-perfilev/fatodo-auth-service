package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:userservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
public class UserServiceCT {

    @Autowired
    UserServiceClient userServiceClient;

    @Test
    void testCreateLocalUserDTO() {
        LocalUserDTO localUserDTO = FactoryUtils.createLocalUserDTO("new", "test_password");
        UserDTO userDTO = userServiceClient.createLocalUser(localUserDTO);
        assertThat(userDTO).isNotNull();
    }

    @Test
    void testCreateOAuth2lUserDTO() {
        OAuth2UserDTO oAuth2UserDTO = FactoryUtils.createOAuth2UserDTO("facebook");
        UserDTO userDTO = userServiceClient.createOAuth2User(oAuth2UserDTO);
        assertThat(userDTO).isNotNull();
    }

    @Test
    void testGetUserPrincipalByUsername() {
        UserPrincipalDTO userPrincipalDTO = userServiceClient.getUserPrincipalByUsername("test_username_local");
        assertThat(userPrincipalDTO).isNotNull();
    }

    @Test
    void testGetUserPrincipalByEmail() {
        UserPrincipalDTO userPrincipalDTO = userServiceClient.getUserPrincipalByEmail("test@email.com");
        assertThat(userPrincipalDTO).isNotNull();
    }

    @Test
    void testActivate() {
        userServiceClient.activate(UUID.randomUUID().toString());
        assertThat(true).isTrue();
    }

}
