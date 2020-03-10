package com.persoff68.fatodo.oauth2;

import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static com.persoff68.fatodo.repository.CookieAuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoAuthServiceApplication.class)
@ExtendWith(MockitoExtension.class)
public class OAuth2AuthorizationEndpointIT {
    private static final String ENDPOINT = "/api/oauth2/authorize";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private Filter springSecurityFilterChain;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    void testAuthorizationEndpoint_facebook() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/facebook"))
                .andExpect(status().isFound())
                .andExpect(header().exists("Location"))
                .andExpect(header().exists("Set-Cookie"));

        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");
        assertThat(locationHeader).contains("www.facebook.com");

        String setCookieHeader = resultActions.andReturn().getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }

    @Test
    void testAuthorizationEndpoint_google() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + "/google"))
                .andExpect(status().isFound())
                .andExpect(header().exists("Location"))
                .andExpect(header().exists("Set-Cookie"));

        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");
        assertThat(locationHeader).contains("accounts.google.com");

        String setCookieHeader = resultActions.andReturn().getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains(OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }

}
