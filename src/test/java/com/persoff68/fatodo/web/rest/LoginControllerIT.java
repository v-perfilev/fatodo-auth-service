package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.web.rest.vm.LoginVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.persoff68.fatodo:userservice:+:stubs")
public class LoginControllerIT {
    private static final String ENDPOINT = "/authenticate";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AppProperties appProperties;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithCustomSecurityContext(jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg")
    void testLogin_correct() throws Exception {
        LoginVM loginVM = LoginVM.builder().username("test_username_1").password("test_password").build();
        String json = this.objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(header().exists(appProperties.getAuth().getAuthorizationHeader()));
    }

    @Test
    @WithCustomSecurityContext(jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJ0ZXN0X3N5c3RlbSIsImF1dGhvcml0aWVzIjoiUk9MRV9TWVNURU0iLCJpYXQiOjAsImV4cCI6MzI1MDM2NzY0MDB9.EV6TMwQSB2XSTnQuB6LQbLETQmWEullfxSOmGDrlsdk93DDWfqr3VQGti6pMmmbUfgCyP9yyWjlWK50dYHYnEg")
    void testLogin_wrong() throws Exception {
        LoginVM loginVM = LoginVM.builder().username("test_username_1").password("wrong_password").build();
        String json = this.objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isUnauthorized());
    }
}
