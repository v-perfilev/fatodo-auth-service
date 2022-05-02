package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.builder.TestCaptchaResponseDTO;
import com.persoff68.fatodo.builder.TestRegisterVM;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.service.exception.ModelDuplicatedException;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
class RegisterControllerIT {
    private static final String ENDPOINT = "/api/account";

    private static final String LOCAL_NAME = "local-name";
    private static final String NEW_NAME = "new-name";

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AppProperties appProperties;
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    MailServiceClient mailServiceClient;
    @MockBean
    CaptchaClient captchaClient;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        UserPrincipalDTO localUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
                .username(LOCAL_NAME)
                .password(passwordEncoder.encode("test_password"))
                .build();

        when(userServiceClient.createLocalUser(argThat((LocalUserDTO dto) ->
                dto != null && NEW_NAME.equals(dto.getUsername()))))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.createLocalUser(argThat((LocalUserDTO dto) ->
                dto != null && LOCAL_NAME.equals(dto.getUsername()))))
                .thenThrow(new ModelDuplicatedException());

        CaptchaResponseDTO captchaResponseDTO = TestCaptchaResponseDTO.defaultBuilder().build();
        when(captchaClient.sendVerificationRequest(any())).thenReturn(captchaResponseDTO);
    }


    @Test
    @WithAnonymousUser
    void testRegister_ok() throws Exception {
        String url = ENDPOINT + "/register";
        RegisterVM vm = TestRegisterVM.defaultBuilder().username(NEW_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testAuthenticate_invalid() throws Exception {
        String url = ENDPOINT + "/register";
        RegisterVM vm = new RegisterVM();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    void testRegister_duplicated() throws Exception {
        String url = ENDPOINT + "/register";
        RegisterVM vm = TestRegisterVM.defaultBuilder().username(LOCAL_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());

    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    void testRegister_forbidden() throws Exception {
        String url = ENDPOINT + "/register";
        RegisterVM vm = TestRegisterVM.defaultBuilder().username(NEW_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
