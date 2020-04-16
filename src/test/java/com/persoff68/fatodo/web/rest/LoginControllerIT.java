package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.LoginVM;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoAuthServiceApplication.class)
public class LoginControllerIT {
    private static final String ENDPOINT = "/api/authenticate";

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

    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        UserPrincipalDTO localUserPrincipalDTO = FactoryUtils.createUserPrincipalDTO("local",
                Provider.Constants.LOCAL_VALUE, passwordEncoder.encode("test_password"));
        UserPrincipalDTO oAuth2UserPrincipalDTO = FactoryUtils.createUserPrincipalDTO("google",
                Provider.Constants.GOOGLE_VALUE, passwordEncoder.encode("test_password"));

        when(userServiceClient.getUserPrincipalByUsername("test_username_local"))
                .thenReturn(localUserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsername("test_username_google"))
                .thenReturn(oAuth2UserPrincipalDTO);
        when(userServiceClient.getUserPrincipalByUsername("test_username_not_exists"))
                .thenThrow(new ModelNotFoundException());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_ok() throws Exception {
        LoginVM loginVM = FactoryUtils.createLoginVM("local", "test_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        ResultActions resultActions = mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().exists(appProperties.getAuth().getAuthorizationHeader()));

        String authHeader = resultActions.andReturn().getResponse().getHeader(appProperties.getAuth().getAuthorizationHeader());
        assertThat(authHeader).startsWith(appProperties.getAuth().getAuthorizationPrefix());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_wrongPassword() throws Exception {
        LoginVM loginVM = FactoryUtils.createLoginVM("local", "wrong_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_wrongProvider() throws Exception {
        LoginVM loginVM = FactoryUtils.createLoginVM("google", "wrong_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_invalid() throws Exception {
        LoginVM loginVM = FactoryUtils.createInvalidLoginVM();
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_notExists() throws Exception {
        LoginVM loginVM = FactoryUtils.createLoginVM("not_exists", "test_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testAuthenticate_forbidden() throws Exception {
        LoginVM loginVM = FactoryUtils.createLoginVM("local", "test_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
