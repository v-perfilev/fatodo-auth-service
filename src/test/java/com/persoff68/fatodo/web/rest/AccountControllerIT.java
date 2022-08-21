package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestActivation;
import com.persoff68.fatodo.builder.TestCaptchaResponseDTO;
import com.persoff68.fatodo.builder.TestForgotPasswordVM;
import com.persoff68.fatodo.builder.TestResetPassword;
import com.persoff68.fatodo.builder.TestResetPasswordVM;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.ForgotPasswordVM;
import com.persoff68.fatodo.model.vm.ResetPasswordVM;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.UserAlreadyActivatedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
@AutoConfigureMockMvc
class AccountControllerIT {
    private static final String ENDPOINT = "/api/account";

    private static final UUID ACTIVATED_ID = UUID.randomUUID();
    private static final UUID UNACTIVATED_ID = UUID.randomUUID();
    private static final UUID ACTIVATED_CODE = UUID.randomUUID();
    private static final UUID UNACTIVATED_CODE = UUID.randomUUID();

    private static final UUID PASSWORD_NOT_RESET_CODE = UUID.randomUUID();
    private static final UUID PASSWORD_RESET_CODE = UUID.randomUUID();

    private static final String LOCAL_NAME = "local-name";
    private static final String NOT_EXISTING_NAME = "not-existing-name";

    @Autowired
    MockMvc mvc;

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
    @MockBean
    EventServiceClient eventServiceClient;
    @MockBean
    WsServiceClient wsServiceClient;
    @MockBean
    CaptchaClient captchaClient;

    @BeforeEach
    void setup() {
        Activation completedActivation = TestActivation.defaultBuilder()
                .userId(ACTIVATED_ID)
                .code(ACTIVATED_CODE)
                .completed(true)
                .build();
        Activation uncompletedActivation = TestActivation.defaultBuilder()
                .userId(UNACTIVATED_ID)
                .code(UNACTIVATED_CODE)
                .completed(false)
                .build();
        activationRepository.save(completedActivation);
        activationRepository.save(uncompletedActivation);

        ResetPassword resetPasswordNotCompleted = TestResetPassword.defaultBuilder()
                .code(PASSWORD_NOT_RESET_CODE)
                .completed(false)
                .build();
        ResetPassword resetPasswordCompleted = TestResetPassword.defaultBuilder()
                .code(PASSWORD_RESET_CODE)
                .completed(true)
                .build();
        resetPasswordRepository.save(resetPasswordNotCompleted);
        resetPasswordRepository.save(resetPasswordCompleted);

        CaptchaResponseDTO captchaResponseDTO = TestCaptchaResponseDTO.defaultBuilder().build();
        when(captchaClient.sendVerificationRequest(any())).thenReturn(captchaResponseDTO);
        doNothing().when(eventServiceClient).addDefaultEvent(any());
        doNothing().when(wsServiceClient).sendEvent(any());
    }

    @AfterEach
    void cleanup() {
        activationRepository.deleteAll();
        resetPasswordRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    void testActivate_ok() throws Exception {
        String url = ENDPOINT + "/activate/" + UNACTIVATED_CODE;
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testActivate_notFound() throws Exception {
        String url = ENDPOINT + "/activate/" + UUID.randomUUID();
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testActivate_conflict() throws Exception {
        doThrow(new UserAlreadyActivatedException()).when(userServiceClient).activate(any());
        String url = ENDPOINT + "/activate/" + ACTIVATED_CODE;
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testActivate_forbidden() throws Exception {
        String url = ENDPOINT + "/activate/" + UUID.randomUUID();
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testSendActivationCode_ok() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(UNACTIVATED_ID).activated(false).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-activation-code/test_username";
        mvc.perform(get(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testSendActivationCode_badRequest_alreadyActivated() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(ACTIVATED_ID).activated(true).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-activation-code/test_username";
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithAnonymousUser
    void testSendActivationCode_notFound() throws Exception {
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenThrow(ModelNotFoundException.class);
        String url = ENDPOINT + "/request-activation-code/test_username";
        mvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testSendActivationCode_conflict() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(ACTIVATED_ID).build();
        dto.setActivated(true);
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);
        String url = ENDPOINT + "/request-activation-code/test_username_activated";
        mvc.perform(get(url))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testSendActivationCode_forbidden() throws Exception {
        String url = ENDPOINT + "/request-activation-code/test_username";
        mvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testResetPassword_ok() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = TestResetPasswordVM.defaultBuilder().code(PASSWORD_NOT_RESET_CODE).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testResetPassword_notFound_notExists() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = TestResetPasswordVM.defaultBuilder().code(UUID.randomUUID()).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testResetPassword_notFound_finished() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = TestResetPasswordVM.defaultBuilder().code(UUID.randomUUID()).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testResetPassword_forbidden() throws Exception {
        doNothing().when(userServiceClient).resetPassword(any());
        ResetPasswordVM vm = TestResetPasswordVM.defaultBuilder().code(PASSWORD_NOT_RESET_CODE).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/reset-password";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testSendResetPasswordCode_ok() throws Exception {
        UserPrincipalDTO userPrincipalDTO = TestUserPrincipleDTO.defaultBuilder()
                .username(LOCAL_NAME).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(userPrincipalDTO);
        ForgotPasswordVM vm = TestForgotPasswordVM.defaultBuilder().user(LOCAL_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/request-reset-password-code";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testSendResetPasswordCode_notFound() throws Exception {
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenThrow(ModelNotFoundException.class);
        ForgotPasswordVM vm = TestForgotPasswordVM.defaultBuilder().user(NOT_EXISTING_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/request-reset-password-code";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_USER")
    void testSendResetPasswordCode_forbidden() throws Exception {
        ForgotPasswordVM vm = TestForgotPasswordVM.defaultBuilder().user(LOCAL_NAME).build();
        String requestBody = objectMapper.writeValueAsString(vm);
        String url = ENDPOINT + "/request-reset-password-code";
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isForbidden());
    }

}
