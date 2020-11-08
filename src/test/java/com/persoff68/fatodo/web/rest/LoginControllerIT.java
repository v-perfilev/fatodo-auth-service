package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.builder.TestCaptchaResponseDTO;
import com.persoff68.fatodo.builder.TestLoginVM;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.web.rest.vm.LoginVM;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
public class LoginControllerIT {
    private static final String ENDPOINT = "/api/authenticate";

    private static final String LOCAL_NAME = "local-name";
    private static final String GOOGLE_NAME = "google-name";
    private static final String NOT_ACTIVATED_NAME = "not-activated-name";
    private static final String NOT_EXISTING_NAME = "not-existing-name";

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
    CaptchaClient captchaClient;

    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        UserPrincipalDTO localUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
                .username(LOCAL_NAME)
                .password(passwordEncoder.encode("test_password"))
                .build();

        UserPrincipalDTO notActivatedUserPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
                .username(NOT_ACTIVATED_NAME)
                .password(passwordEncoder.encode("test_password"))
                .activated(false)
                .build();

        UserPrincipalDTO oAuth2UserPrincipalDTO = TestUserPrincipleDTO.builder()
                .username(GOOGLE_NAME)
                .provider(Provider.GOOGLE.getValue())
                .build();

        when(userServiceClient.getUserPrincipalByUsernameOrEmail(LOCAL_NAME))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(NOT_ACTIVATED_NAME))
                .thenReturn(notActivatedUserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(GOOGLE_NAME))
                .thenReturn(oAuth2UserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(NOT_EXISTING_NAME))
                .thenThrow(new ModelNotFoundException());

        CaptchaResponseDTO captchaResponseDTO = TestCaptchaResponseDTO.defaultBuilder().build();
        when(captchaClient.sendVerificationRequest(any())).thenReturn(captchaResponseDTO);
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_ok_username() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(LOCAL_NAME).password("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().exists(appProperties.getAuth().getAuthorizationHeader()));
        String authHeader = resultActions.andReturn().getResponse().getHeader(appProperties.getAuth().getAuthorizationHeader());
        assertThat(authHeader).startsWith(appProperties.getAuth().getAuthorizationPrefix());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_ok_email() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(LOCAL_NAME).password("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().exists(appProperties.getAuth().getAuthorizationHeader()));
        String authHeader = resultActions.andReturn().getResponse().getHeader(appProperties.getAuth().getAuthorizationHeader());
        assertThat(authHeader).startsWith(appProperties.getAuth().getAuthorizationPrefix());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_badRequest_wrongPassword() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(LOCAL_NAME).password("wrong_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_badRequest_wrongProvider() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(GOOGLE_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_locked_notActivated() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(NOT_ACTIVATED_NAME).password("test_password").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isLocked());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_invalid() throws Exception {
        LoginVM vm = new LoginVM();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_notExists() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(NOT_EXISTING_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testAuthenticate_forbidden() throws Exception {
        LoginVM vm = TestLoginVM.defaultBuilder().user(LOCAL_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
