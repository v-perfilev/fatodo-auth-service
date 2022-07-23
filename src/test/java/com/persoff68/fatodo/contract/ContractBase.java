package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestActivation;
import com.persoff68.fatodo.builder.TestCaptchaResponseDTO;
import com.persoff68.fatodo.builder.TestResetPassword;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
class ContractBase {

    private static final UUID LOCAL_ID = UUID.randomUUID();
    private static final String LOCAL_NAME = "local-name";
    private static final String NOT_ACTIVATED_NAME = "not-activated-name";
    private static final UUID NOT_ACTIVATED_CODE = UUID.fromString("34ba7ebf-a43c-4a37-813d-b5a401948857");
    private static final UUID PASSWORD_NOT_RESET_CODE = UUID.fromString("34ba7ebf-a43c-4a37-813d-b5a401948857");

    @Autowired
    WebApplicationContext context;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ActivationRepository activationRepository;
    @Autowired
    ResetPasswordRepository resetPasswordRepository;
    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    MailServiceClient mailServiceClient;
    @MockBean
    EventServiceClient eventServiceClient;
    @MockBean
    CaptchaClient captchaClient;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);

        Activation activation = TestActivation.defaultBuilder()
                .userId(LOCAL_ID)
                .code(NOT_ACTIVATED_CODE)
                .completed(false)
                .build();
        activationRepository.save(activation);

        ResetPassword resetPassword = TestResetPassword.defaultBuilder()
                .id(LOCAL_ID)
                .code(PASSWORD_NOT_RESET_CODE)
                .completed(false)
                .build();
        resetPasswordRepository.save(resetPassword);

        UserPrincipalDTO localUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
                .username(LOCAL_NAME)
                .password(passwordEncoder.encode("test_password"))
                .build();

        UserPrincipalDTO notActivatedUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
                .username(NOT_ACTIVATED_NAME)
                .activated(false)
                .build();

        when(userServiceClient.getUserPrincipalByUsernameOrEmail(localUserPrincipalDTO.getUsername()))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(localUserPrincipalDTO.getEmail()))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(notActivatedUserPrincipalDTO.getUsername()))
                .thenReturn(notActivatedUserPrincipalDTO);

        when(userServiceClient.createLocalUser(any())).thenReturn(localUserPrincipalDTO);

        CaptchaResponseDTO captchaResponseDTO = TestCaptchaResponseDTO.defaultBuilder().build();
        when(captchaClient.sendVerificationRequest(any())).thenReturn(captchaResponseDTO);
    }

    @AfterEach
    void cleanup() {
        activationRepository.deleteAll();
        resetPasswordRepository.deleteAll();
    }

}
