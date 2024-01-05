package com.persoff68.fatodo.oauth2;

import com.persoff68.fatodo.FatodoAuthServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static com.persoff68.fatodo.security.oauth2.repository.CookieAuthorizationRequestRepository.OAUTH2_REQUEST_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
@ExtendWith(MockitoExtension.class)
class OAuth2AuthorizationEndpointIT {
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

    @ParameterizedTest
    @CsvSource({
            "/google, accounts.google.com",
            "/apple, apple.com",
    })
    void testAuthorizationEndpoint_google(String path, String uri) throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT + path))
                .andExpect(status().isFound())
                .andExpect(header().exists("Location"))
                .andExpect(header().exists("Set-Cookie"));

        String locationHeader = resultActions.andReturn().getResponse().getHeader("Location");
        assertThat(locationHeader).contains(uri);

        String setCookieHeader = resultActions.andReturn().getResponse().getHeader("Set-Cookie");
        assertThat(setCookieHeader).contains(OAUTH2_REQUEST_COOKIE_NAME);
    }

}
