package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.persoff68.fatodo:userservice:+:stubs")
public class RegisterControllerIT {
    private static final String ENDPOINT = "/register";

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceClient userServiceClient;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testRegisterLocal_correct() throws Exception {
        RegisterVM registerVM = RegisterVM.builder()
                .email("test_2@email.com")
                .username("test_username_2")
                .password("test_password")
                .build();
        String json = this.objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterLocal_wrong() throws Exception {
        RegisterVM registerVM = RegisterVM.builder()
                .email("test_2@email.com")
                .password("test_password")
                .build();
        String json = this.objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
