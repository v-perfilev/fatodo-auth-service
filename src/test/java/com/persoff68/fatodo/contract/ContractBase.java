package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public class ContractBase {

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

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);

        activationRepository.deleteAll();
        Activation activation = FactoryUtils.createActivation("test_user_local", "1");
        activationRepository.save(activation);

        resetPasswordRepository.deleteAll();
        ResetPassword resetPassword = FactoryUtils.createResetPassword("test_username_local",
                "1", false);
        resetPasswordRepository.save(resetPassword);

        UserPrincipalDTO localUserPrincipalDTO = FactoryUtils.createUserPrincipalDTO("local",
                Provider.Constants.LOCAL_VALUE, passwordEncoder.encode("test_password"));
        when(userServiceClient.getUserPrincipalByUsername(localUserPrincipalDTO.getUsername()))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByEmail(localUserPrincipalDTO.getEmail()))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.createLocalUser(any())).thenReturn(localUserPrincipalDTO);
    }

}
