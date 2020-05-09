package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.model.dto.ActivationDTO;
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
    void testActivate() {
        ActivationDTO dto = FactoryUtils.createActivationDTO();
        mailServiceClient.activate(dto);
        assertThat(true);
    }

}
