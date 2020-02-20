package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.constant.AuthProvider;
import com.persoff68.fatodo.web.rest.vm.LoginVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoAuthServiceApplication.class)
@ExtendWith(MockitoExtension.class)
public class LoginControllerIT {
    private static final String BASE_API = "/authenticate";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AppProperties appProperties;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    UserServiceClient userServiceClient;

    UserPrincipal testUserPrincipal;
    LoginVM testLoginVM;
    LoginVM testWrongLoginVM;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        testUserPrincipal = createTestUserPrincipal(passwordEncoder);
        testLoginVM = createTestLoginVM();
        testWrongLoginVM = createWrongTestLoginVM();
    }

    @Test
    void testLogin_correct() throws Exception {
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(testUserPrincipal);
        String json = this.objectMapper.writeValueAsString(testLoginVM);

        mvc.perform(post(BASE_API)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists(appProperties.getAuth().getAuthorizationHeader()));
    }

    @Test
    void testLogin_wrong() throws Exception {
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(testUserPrincipal);
        String json = this.objectMapper.writeValueAsString(testWrongLoginVM);

        mvc.perform(post(BASE_API)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    private static LoginVM createTestLoginVM() {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("test_username");
        loginVM.setPassword("test_password");
        return loginVM;
    }

    private static LoginVM createWrongTestLoginVM() {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("test_username");
        loginVM.setPassword("wrong_password");
        return loginVM;
    }

    private static UserPrincipal createTestUserPrincipal(PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("test_password");

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId("test_id");
        userPrincipal.setEmail("test@email.test");
        userPrincipal.setUsername("test_username");
        userPrincipal.setPassword(password);
        userPrincipal.setProvider(AuthProvider.LOCAL);
        userPrincipal.setAuthorities(Collections.singleton("ROLE_USER"));
        return userPrincipal;
    }
}
