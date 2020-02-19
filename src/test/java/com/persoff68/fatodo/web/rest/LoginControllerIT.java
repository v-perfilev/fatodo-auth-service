package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoAuthServiceApplication.class)
@TestPropertySource("classpath:application.yml")
public class LoginControllerIT {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testRegisterLocal() throws Exception {
        RegisterVM registerVM = new RegisterVM();
        registerVM.setUsername("test_username");
        registerVM.setEmail("test@email.test");
        registerVM.setPassword("test_password");

        ResultActions result = mvc.perform(post("/register", registerVM))
                .andExpect(status().isOk());
        String resultString = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultString);
    }

}
