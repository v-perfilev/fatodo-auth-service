package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.ResetPasswordVM;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
public class AccountControllerIT {
    private static final String ENDPOINT = "/api/account";

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ActivationRepository activationRepository;
    @Autowired
    ResetPasswordRepository resetPasswordRepository;
    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    MailServiceClient mailServiceClient;

    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        Activation activation;
        activationRepository.deleteAll();
        activation = FactoryUtils.createActivation("test_username_activated", "1", true);
        activationRepository.save(activation);
        activation = FactoryUtils.createActivation("test_username_new", "2", false);
        activationRepository.save(activation);

        ResetPassword resetPassword;
        resetPasswordRepository.deleteAll();
        resetPassword = FactoryUtils.createResetPassword("test_user_1", "1", false);
        resetPasswordRepository.save(resetPassword);
        resetPassword = FactoryUtils.createResetPassword("test_user_2", "2", true);
        resetPasswordRepository.save(resetPassword);
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
    public void testActivate_notFound() throws Exception {
        String url = ENDPOINT + "/activate/notExists";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void testActivate_conflict() throws Exception {
        doThrow(new UserAlreadyActivatedException()).when(userServiceClient).activate(any());
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

    @Test
    @WithAnonymousUser
    public void testSendActivationCode_ok() throws Exception {
        UserPrincipalDTO dto = FactoryUtils.createUserPrincipalDTO("_new",
                Provider.Constants.LOCAL_VALUE, "test_password", false);
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(dto);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-activation-code/test_username_new";
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testSendActivationCode_badRequest_alreadyActivated() throws Exception {
        UserPrincipalDTO dto = FactoryUtils.createUserPrincipalDTO("_new",
                Provider.Constants.LOCAL_VALUE, "test_password");
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(dto);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-activation-code/test_username_new";
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithAnonymousUser
    public void testSendActivationCode_notFound() throws Exception {
        when(userServiceClient.getUserPrincipalByUsername(any())).thenThrow(ModelNotFoundException.class);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenThrow(ModelNotFoundException.class);
        String url = ENDPOINT + "/request-activation-code/test_username_notFound";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void testSendActivationCode_conflict() throws Exception {
        UserPrincipalDTO dto = FactoryUtils.createUserPrincipalDTO("_activated",
                Provider.Constants.LOCAL_VALUE, "test_password");
        dto.setActivated(true);
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(dto);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-activation-code/test_username_activated";
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testSendActivationCode_forbidden() throws Exception {
        String url = ENDPOINT + "/request-activation-code/test_username_new";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void testResetPassword_ok() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = FactoryUtils.createResetPasswordVM("1");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testResetPassword_notFound_notExists() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = FactoryUtils.createResetPasswordVM("3");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    public void testResetPassword_notFound_finished() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = FactoryUtils.createResetPasswordVM("2");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testResetPassword_forbidden() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = FactoryUtils.createResetPasswordVM("1");
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void testSendResetPasswordCode_ok() throws Exception {
        UserPrincipalDTO dto = FactoryUtils.createUserPrincipalDTO("_new",
                Provider.Constants.LOCAL_VALUE, "test_password");
        when(userServiceClient.getUserPrincipalByUsername(any())).thenReturn(dto);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-reset-password-code/test_username_new";
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testSendResetPasswordCode_notFound() throws Exception {
        when(userServiceClient.getUserPrincipalByUsername(any())).thenThrow(ModelNotFoundException.class);
        when(userServiceClient.getUserPrincipalByEmail(any())).thenThrow(ModelNotFoundException.class);
        String url = ENDPOINT + "/request-reset-password-code/test_username_notFound";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = AuthorityType.Constants.USER_VALUE)
    public void testSendResetPasswordCode_forbidden() throws Exception {
        String url = ENDPOINT + "/request-reset-password-code/test_username_new";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

}
