package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestLocalUserDTO;
import com.persoff68.fatodo.builder.TestOAuth2UserDTO;
import com.persoff68.fatodo.builder.TestResetPasswordDTO;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:userservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class UserServiceCT {

    @Autowired
    UserServiceClient userServiceClient;

    @Test
    void testCreateLocalUserDTO() {
        LocalUserDTO localUserDTO = TestLocalUserDTO.defaultBuilder().build();
        UserPrincipalDTO dto = userServiceClient.createLocalUser(localUserDTO);
        assertThat(dto).isNotNull();
    }

    @Test
    void testCreateOAuth2lUserDTO() {
        OAuth2UserDTO oAuth2UserDTO = TestOAuth2UserDTO.defaultBuilder().build();
        UserPrincipalDTO dto = userServiceClient.createOAuth2User(oAuth2UserDTO);
        assertThat(dto).isNotNull();
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
    void testGetUserPrincipalByUsernameOrEmail_username() {
        UserPrincipalDTO userPrincipalDTO = userServiceClient
                .getUserPrincipalByUsernameOrEmail("test_username_local");
        assertThat(userPrincipalDTO).isNotNull();
    }

    @Test
    void testGetUserPrincipalByUsernameOrEmail_email() {
        UserPrincipalDTO userPrincipalDTO = userServiceClient
                .getUserPrincipalByUsernameOrEmail("test@email.com");
        assertThat(userPrincipalDTO).isNotNull();
    }

    @Test
    void testActivate() {
        UUID id = UUID.randomUUID();
        assertDoesNotThrow(() -> userServiceClient.activate(id));
    }

    @Test
    void testResetPassword() {
        ResetPasswordDTO dto = TestResetPasswordDTO.defaultBuilder().build();
        assertDoesNotThrow(() -> userServiceClient.resetPassword(dto));
    }

}
