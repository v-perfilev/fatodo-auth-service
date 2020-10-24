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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:mailservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
public class MailServiceCT {

    @Autowired
    MailServiceClient mailServiceClient;

    @Test
    void testSendActivationCode() {
        ActivationMailDTO dto = TestActivationMailDTO.defaultBuilder().build();
        mailServiceClient.sendActivationCode(dto);
        assertThat(true).isTrue();
    }

    @Test
    void testSendResetPasswordCode() {
        ResetPasswordMailDTO dto = TestResetPasswordMailDTO.defaultBuilder().build();
        mailServiceClient.sendResetPasswordCode(dto);
        assertThat(true).isTrue();
    }

}
