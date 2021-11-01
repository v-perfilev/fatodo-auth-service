package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestActivationMail;
import com.persoff68.fatodo.builder.TestResetPasswordMail;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.model.ActivationMail;
import com.persoff68.fatodo.model.ResetPasswordMail;
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
        ActivationMail dto = TestActivationMail.defaultBuilder().build();
        mailServiceClient.sendActivationCode(dto);
        assertThat(true).isTrue();
    }

    @Test
    void testSendResetPasswordCode() {
        ResetPasswordMail dto = TestResetPasswordMail.defaultBuilder().build();
        mailServiceClient.sendResetPasswordCode(dto);
        assertThat(true).isTrue();
    }

}
