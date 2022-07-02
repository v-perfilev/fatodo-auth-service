package com.persoff68.fatodo.kafka.producer;

import com.persoff68.fatodo.builder.TestActivation;
import com.persoff68.fatodo.builder.TestResetPassword;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ActivationMail;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.ResetPasswordMail;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import com.persoff68.fatodo.service.AccountService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "kafka.bootstrapAddress=PLAINTEXT://localhost:9092",
        "kafka.groupId=test",
        "kafka.partitions=1"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class MailProducerIT {

    private static final UUID UNACTIVATED_ID = UUID.randomUUID();
    private static final UUID UNACTIVATED_CODE = UUID.randomUUID();
    private static final UUID PASSWORD_NOT_RESET_CODE = UUID.randomUUID();

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    AccountService accountService;

    @Autowired
    ActivationRepository activationRepository;
    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @MockBean
    UserServiceClient userServiceClient;

    @SpyBean
    MailServiceClient mailServiceClient;

    private ConcurrentMessageListenerContainer<String, ActivationMail> activationContainer;
    private BlockingQueue<ConsumerRecord<String, ActivationMail>> activationRecords;

    private ConcurrentMessageListenerContainer<String, ResetPasswordMail> resetPasswordContainer;
    private BlockingQueue<ConsumerRecord<String, ResetPasswordMail>> resetPasswordRecords;

    @BeforeEach
    void setup() {
        Activation uncompletedActivation = TestActivation.defaultBuilder()
                .userId(UNACTIVATED_ID)
                .code(UNACTIVATED_CODE)
                .completed(false)
                .build();
        activationRepository.save(uncompletedActivation);

        ResetPassword resetPasswordNotCompleted = TestResetPassword.defaultBuilder()
                .code(PASSWORD_NOT_RESET_CODE)
                .completed(false)
                .build();
        resetPasswordRepository.save(resetPasswordNotCompleted);

        startActivationConsumer();
        startResetPasswordConsumer();
    }

    @AfterEach
    void cleanup() {
        activationRepository.deleteAll();
        resetPasswordRepository.deleteAll();

        stopActivationConsumer();
        stopResetPasswordConsumer();
    }

    @Test
    void testSendActivationCode_ok() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(UNACTIVATED_ID).activated(false).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);

        accountService.sendActivationCodeMail("test_username");

        ConsumerRecord<String, ActivationMail> record = activationRecords.poll(10, TimeUnit.SECONDS);

        assertThat(mailServiceClient instanceof MailProducer).isTrue();
        assertThat(record).isNotNull();
        verify(mailServiceClient).sendActivationCode(any());
    }

    @Test
    void testSendResetPasswordCode_ok() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(UNACTIVATED_ID).activated(false).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);

        accountService.sendResetPasswordMail("test_username");

        ConsumerRecord<String, ResetPasswordMail> record = resetPasswordRecords.poll(10, TimeUnit.SECONDS);

        assertThat(mailServiceClient instanceof MailProducer).isTrue();
        assertThat(record).isNotNull();
        verify(mailServiceClient).sendResetPasswordCode(any());
    }

    private void startActivationConsumer() {
        activationContainer = KafkaUtils.buildJsonContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test",
                ActivationMail.class).createContainer("mail_activation");
        activationRecords = new LinkedBlockingQueue<>();
        activationContainer.setupMessageListener((MessageListener<String, ActivationMail>) activationRecords::add);
        activationContainer.start();
        ContainerTestUtils.waitForAssignment(activationContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopActivationConsumer() {
        activationContainer.stop();
    }

    private void startResetPasswordConsumer() {
        resetPasswordContainer = KafkaUtils.buildJsonContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test",
                ResetPasswordMail.class).createContainer("mail_resetPassword");
        resetPasswordRecords = new LinkedBlockingQueue<>();
        resetPasswordContainer.setupMessageListener((MessageListener<String, ResetPasswordMail>) resetPasswordRecords::add);
        resetPasswordContainer.start();
        ContainerTestUtils.waitForAssignment(resetPasswordContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopResetPasswordConsumer() {
        resetPasswordContainer.stop();
    }

}
