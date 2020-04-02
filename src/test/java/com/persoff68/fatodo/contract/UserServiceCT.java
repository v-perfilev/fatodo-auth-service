package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.FactoryUtils;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.interceptor.JwtInterceptor;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:userservice:+:stubs:6565"}, stubsMode = StubRunnerProperties.StubsMode.REMOTE)
public class UserServiceCT {

    @Autowired
    UserServiceClient userServiceClient;

    @Test
    void testSomething() {
        LocalUserDTO localUserDTO = FactoryUtils.createLocalUserDTO("new", "test_password");
        UserDTO userDTO = userServiceClient.createLocalUser(localUserDTO);
        assertThat(userDTO).isNotNull();
    }

}
