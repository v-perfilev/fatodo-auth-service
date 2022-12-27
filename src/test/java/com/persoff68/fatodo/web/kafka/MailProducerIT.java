package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.builder.TestActivation;
import com.persoff68.fatodo.builder.TestResetPassword;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.MailServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.repository.ResetPasswordRepository;
import com.persoff68.fatodo.service.AccountService;
import com.persoff68.fatodo.service.FeedbackService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
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
        "kafka.bootstrapAddress=localhost:9092",
        "kafka.groupId=test",
        "kafka.partitions=1",
        "kafka.autoOffsetResetConfig=earliest"
})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class MailProducerIT {

    private static final UUID UNACTIVATED_ID = UUID.randomUUID();
    private static final UUID UNACTIVATED_CODE = UUID.randomUUID();
    private static final UUID PASSWORD_NOT_RESET_CODE = UUID.randomUUID();

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    AccountService accountService;
    @Autowired
    FeedbackService feedbackService;

    @Autowired
    ActivationRepository activationRepository;
    @Autowired
    ResetPasswordRepository resetPasswordRepository;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceClient userServiceClient;

    @SpyBean
    MailServiceClient mailServiceClient;

    private ConcurrentMessageListenerContainer<String, String> authContainer;
    private BlockingQueue<ConsumerRecord<String, String>> authRecords;

    private ConcurrentMessageListenerContainer<String, String> feedbackContainer;
    private BlockingQueue<ConsumerRecord<String, String>> feedbackRecords;

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

        startAuthConsumer();
        startFeedbackConsumer();
    }

    @AfterEach
    void cleanup() {
        activationRepository.deleteAll();
        resetPasswordRepository.deleteAll();

        stopAuthConsumer();
        stopFeedbackConsumer();
    }

    @Test
    void testSendActivationCode_ok() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(UNACTIVATED_ID).activated(false).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);

        accountService.sendActivationCodeMail("test_username");

        ConsumerRecord<String, String> record = authRecords.poll(5, TimeUnit.SECONDS);

        assertThat(mailServiceClient).isInstanceOf(MailProducer.class);
        assertThat(record).isNotNull();
        assertThat(record.key()).isEqualTo("activation");
        verify(mailServiceClient).sendActivationCode(any());
    }

    @Test
    void testSendResetPasswordCode_ok() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(UNACTIVATED_ID).activated(false).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);

        accountService.sendResetPasswordMail("test_username");

        ConsumerRecord<String, String> record = authRecords.poll(5, TimeUnit.SECONDS);

        assertThat(mailServiceClient).isInstanceOf(MailProducer.class);
        assertThat(record).isNotNull();
        assertThat(record.key()).isEqualTo("reset-password");
        verify(mailServiceClient).sendResetPasswordCode(any());
    }

    @Test
    void testSendFeedback_ok() throws Exception {
        feedbackService.sendFeedback("test", "test@test.test", "test");

        ConsumerRecord<String, String> record = feedbackRecords.poll(5, TimeUnit.SECONDS);

        assertThat(mailServiceClient).isInstanceOf(MailProducer.class);
        assertThat(record).isNotNull();
        verify(mailServiceClient).sendFeedback(any());
    }


    private void startAuthConsumer() {
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory =
                KafkaUtils.buildStringContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test", "earliest");
        authContainer = containerFactory.createContainer("mail_auth");
        authRecords = new LinkedBlockingQueue<>();
        authContainer.setupMessageListener((MessageListener<String, String>) authRecords::add);
        authContainer.start();
        ContainerTestUtils.waitForAssignment(authContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopAuthConsumer() {
        authContainer.stop();
    }


    private void startFeedbackConsumer() {
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory =
                KafkaUtils.buildStringContainerFactory(embeddedKafkaBroker.getBrokersAsString(), "test", "earliest");
        feedbackContainer = containerFactory.createContainer("mail_feedback");
        feedbackRecords = new LinkedBlockingQueue<>();
        feedbackContainer.setupMessageListener((MessageListener<String, String>) feedbackRecords::add);
        feedbackContainer.start();
        ContainerTestUtils.waitForAssignment(feedbackContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopFeedbackConsumer() {
        feedbackContainer.stop();
    }

}
