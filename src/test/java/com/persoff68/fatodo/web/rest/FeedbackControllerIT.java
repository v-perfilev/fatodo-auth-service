package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoAuthServiceApplication;
import com.persoff68.fatodo.builder.TestCaptchaResponseDTO;
import com.persoff68.fatodo.builder.TestFeedbackVM;
import com.persoff68.fatodo.client.CaptchaClient;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.model.dto.CaptchaResponseDTO;
import com.persoff68.fatodo.model.vm.FeedbackVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoAuthServiceApplication.class)
@AutoConfigureMockMvc
class FeedbackControllerIT {
    private static final String ENDPOINT = "/api/feedback";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    MailServiceClient mailServiceClient;
    @MockBean
    CaptchaClient captchaClient;

    @BeforeEach
    void setup() {
        CaptchaResponseDTO captchaResponseDTO = TestCaptchaResponseDTO.defaultBuilder().build();
        when(captchaClient.sendVerificationRequest(any())).thenReturn(captchaResponseDTO);
    }

    @Test
    @WithAnonymousUser
    void testSendFeedback_ok() throws Exception {
        FeedbackVM vm = TestFeedbackVM.defaultBuilder().build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void testSendFeedback_invalid() throws Exception {
        FeedbackVM vm = TestFeedbackVM.defaultBuilder().name("").email("").message("").build();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

}
