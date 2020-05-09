package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.repository.ActivationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
public class ActivationControllerIT {
    private static final String ENDPOINT = "/api/activation";

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AppProperties appProperties;
    @Autowired
    ActivationRepository activationRepository;
    @MockBean
    UserServiceClient userServiceClient;

    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        activationRepository.deleteAll();
        Activation activationActivated = FactoryUtils.createActivation("1", true);
        activationRepository.save(activationActivated);
        Activation activationNotActivated = FactoryUtils.createActivation("2", false);
        activationRepository.save(activationNotActivated);
    }

    @Test
    @WithAnonymousUser
    public void testActivate_ok() throws Exception {
        String url = ENDPOINT + "/activate/2";
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testActivate_conflict() throws Exception {
        String url = ENDPOINT + "/activate/1";
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testActivate_forbidden() throws Exception {
        String url = ENDPOINT + "/activate/" + UUID.randomUUID().toString();
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }


}
