package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestActivationMailDTO;
import com.persoff68.fatodo.builder.TestResetPasswordMailDTO;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:mailservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class MailServiceCT {

    @Autowired
    MailServiceClient mailServiceClient;

    @Test
    void testSendActivationCode() {
        ActivationMailDTO dto = TestActivationMailDTO.defaultBuilder().build();
        assertDoesNotThrow(() -> mailServiceClient.sendActivationCode(dto));
    }

    @Test
    void testSendResetPasswordCode() {
        ResetPasswordMailDTO dto = TestResetPasswordMailDTO.defaultBuilder().build();
        assertDoesNotThrow(() -> mailServiceClient.sendResetPasswordCode(dto));
    }

}
