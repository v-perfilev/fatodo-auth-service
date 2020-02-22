package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.constant.AuthProvider;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoAuthServiceApplication.class)
@ExtendWith(MockitoExtension.class)
public class RegisterControllerIT {
    private static final String ENDPOINT = "/register";

    @Autowired
    WebApplicationContext context;
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceClient userServiceClient;

    UserDTO testUserDTO;
    RegisterVM testRegisterVM;
    RegisterVM testWrongRegisterVM;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        testUserDTO = createTestUserDTO();
        testRegisterVM = createTestRegisterVM();
        testWrongRegisterVM = createWrongTestRegisterVM();
    }

    @Test
    void testRegisterLocal_correct() throws Exception {
        when(userServiceClient.createLocalUser(any())).thenReturn(testUserDTO);
        String json = this.objectMapper.writeValueAsString(testRegisterVM);

        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterLocal_wrong() throws Exception {
        String json = this.objectMapper.writeValueAsString(testWrongRegisterVM);

        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    private static UserDTO createTestUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test_id");
        userDTO.setEmail("test@email.test");
        userDTO.setUsername("test_username");
        userDTO.setProvider(AuthProvider.LOCAL);
        userDTO.setAuthorities(Collections.singleton("ROLE_USER"));
        return userDTO;
    }

    private static RegisterVM createTestRegisterVM() {
        RegisterVM registerVM = new RegisterVM();
        registerVM.setUsername("test_username");
        registerVM.setEmail("test@email.test");
        registerVM.setPassword("test_password");
        return registerVM;
    }

    private static RegisterVM createWrongTestRegisterVM() {
        RegisterVM registerVM = new RegisterVM();
        registerVM.setEmail("test@email.test");
        registerVM.setPassword("test_password");
        return registerVM;
    }
}
