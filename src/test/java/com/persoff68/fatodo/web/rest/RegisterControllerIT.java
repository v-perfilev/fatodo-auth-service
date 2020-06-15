package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.LoginVM;
import com.persoff68.fatodo.model.vm.RegisterVM;
import com.persoff68.fatodo.service.exception.ModelDuplicatedException;
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
public class RegisterControllerIT {
    private static final String ENDPOINT = "/api/register";

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
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        UserPrincipalDTO localUserPrincipalDTO = FactoryUtils.createUserPrincipalDTO("local",
                Provider.LOCAL.getValue(), passwordEncoder.encode("test_password"));
        when(userServiceClient.createLocalUser(argThat((LocalUserDTO dto) ->
                dto != null && "test_username_new".equals(dto.getUsername()))))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.createLocalUser(argThat((LocalUserDTO dto) ->
                dto != null && "test_username_local".equals(dto.getUsername()))))
                .thenThrow(new ModelDuplicatedException());

        CaptchaResponseDTO captchaResponseDTO = FactoryUtils.createCaptchaResponseDTO(true);
        when(captchaClient.sendVerificationRequest(any())).thenReturn(captchaResponseDTO);
    }


    @Test
    @WithAnonymousUser
    public void testRegister_ok() throws Exception {
        RegisterVM registerVM = FactoryUtils.createRegisterVM("new", "test_password");
        String requestBody = objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_invalid() throws Exception {
        RegisterVM registerVM = FactoryUtils.createInvalidRegisterVM();
        String requestBody = objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testRegister_duplicated() throws Exception {
        RegisterVM registerVM = FactoryUtils.createRegisterVM("local", "test_password");
        String requestBody = objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());

    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testRegister_forbidden() throws Exception {
        LoginVM loginVM = FactoryUtils.createUsernameLoginVM("new", "test_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
