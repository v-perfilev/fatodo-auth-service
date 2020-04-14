package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FaToDoAuthServiceApplication;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.web.rest.vm.LoginVM;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FaToDoAuthServiceApplication.class)
public class RegisterControllerIT {
    private static final String ENDPOINT = "/api/register";

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AppProperties appProperties;
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    UserServiceClient userServiceClient;

    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        UserDTO userDTO = FactoryUtils.createUserDTO("new", Provider.Constants.LOCAL_VALUE);
        when(userServiceClient.createLocalUser(argThat((LocalUserDTO dto) ->
                dto != null && "test_username_new".equals(dto.getUsername()))))
                .thenReturn(userDTO);
        when(userServiceClient.createLocalUser(argThat((LocalUserDTO dto) ->
                dto != null && "test_username_local".equals(dto.getUsername()))))
                .thenThrow(FeignException.Conflict.class);
    }


    @Test
    @WithAnonymousUser
    public void testRegister_ok() throws Exception {
        RegisterVM registerVM = FactoryUtils.createRegisterVM("new", "test_password");
        String requestBody = objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testAuthenticate_invalid() throws Exception {
        RegisterVM registerVM = FactoryUtils.createInvalidRegisterVM();
        String requestBody = objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithAnonymousUser
    public void testRegister_duplicated() throws Exception {
        RegisterVM registerVM = FactoryUtils.createRegisterVM("local", "test_password");
        String requestBody = objectMapper.writeValueAsString(registerVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());

    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testRegister_forbidden() throws Exception {
        LoginVM loginVM = FactoryUtils.createLoginVM("new", "test_password");
        String requestBody = objectMapper.writeValueAsString(loginVM);
        mvc.perform(post(ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
