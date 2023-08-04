package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
@AutoConfigureMockMvc
class HealthControllerIT {
    private static final String ENDPOINT = "/api/health";

    @Autowired
    MockMvc mvc;

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testCheck_authorized_ok() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testCheck_unauthorized_ok() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
    }

}
