package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.Providers;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
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
    private static final String ENDPOINT = "/authenticate";

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

    UserPrincipalDTO userPrincipalDTO;
    LoginVM testLoginVM;
    LoginVM testWrongLoginVM;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        userPrincipalDTO = createTestUserPrincipalDTO(passwordEncoder);
        testLoginVM = createTestLoginVM();
        testWrongLoginVM = createWrongTestLoginVM();
    }

    @Test
    void testLogin_correct() throws Exception {
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(userPrincipalDTO);
        String json = this.objectMapper.writeValueAsString(testLoginVM);

        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists(appProperties.getAuth().getAuthorizationHeader()));
    }

    @Test
    void testLogin_wrong() throws Exception {
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(userPrincipalDTO);
        String json = this.objectMapper.writeValueAsString(testWrongLoginVM);

        mvc.perform(post(ENDPOINT)
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

    private static UserPrincipalDTO createTestUserPrincipalDTO(PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode("test_password");

        UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO();
        userPrincipalDTO.setId("test_id");
        userPrincipalDTO.setEmail("test@email.test");
        userPrincipalDTO.setUsername("test_username");
        userPrincipalDTO.setPassword(password);
        userPrincipalDTO.setProvider(Providers.LOCAL);
        userPrincipalDTO.setAuthorities(Collections.singleton("ROLE_USER"));
        return userPrincipalDTO;
    }
}
