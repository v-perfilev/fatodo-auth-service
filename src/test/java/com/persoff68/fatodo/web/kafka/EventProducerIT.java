package com.persoff68.fatodo.web.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.builder.TestActivation;
import com.persoff68.fatodo.builder.TestUserPrincipleDTO;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.repository.ActivationRepository;
import com.persoff68.fatodo.service.AccountService;
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
class EventProducerIT {

    private static final UUID UNACTIVATED_ID = UUID.randomUUID();
    private static final UUID UNACTIVATED_CODE = UUID.randomUUID();

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    AccountService accountService;
    @Autowired
    ActivationRepository activationRepository;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    WsServiceClient wsServiceClient;

    @SpyBean
    EventServiceClient eventServiceClient;

    private ConcurrentMessageListenerContainer<String, EventDTO> eventContainer;
    private BlockingQueue<ConsumerRecord<String, EventDTO>> eventRecords;

    @BeforeEach
    void setup() {
        Activation uncompletedActivation = TestActivation.defaultBuilder()
                .userId(UNACTIVATED_ID)
                .code(UNACTIVATED_CODE)
                .completed(false)
                .build();
        activationRepository.save(uncompletedActivation);

        startEventConsumer();
    }

    @AfterEach
    void cleanup() {
        activationRepository.deleteAll();

        stopEventConsumer();
    }

    @Test
    void testAddEvent_ok() throws Exception {
        UserPrincipalDTO dto = TestUserPrincipleDTO.defaultBuilder().id(UNACTIVATED_ID).activated(false).build();
        when(userServiceClient.getUserPrincipalByUsernameOrEmail(any())).thenReturn(dto);

        accountService.activate(UNACTIVATED_CODE);

        ConsumerRecord<String, EventDTO> record = eventRecords.poll(5, TimeUnit.SECONDS);

        assertThat(eventServiceClient).isInstanceOf(EventProducer.class);
        assertThat(record).isNotNull();
        verify(eventServiceClient).addEvent(any());
    }

    private void startEventConsumer() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(EventDTO.class);
        ConcurrentKafkaListenerContainerFactory<String, EventDTO> сontainerFactory =
                KafkaUtils.buildJsonContainerFactory(embeddedKafkaBroker.getBrokersAsString(),
                        "test", "earliest", javaType);
        eventContainer = сontainerFactory.createContainer("event");
        eventRecords = new LinkedBlockingQueue<>();
        eventContainer.setupMessageListener((MessageListener<String, EventDTO>) eventRecords::add);
        eventContainer.start();
        ContainerTestUtils.waitForAssignment(eventContainer, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    private void stopEventConsumer() {
        eventContainer.stop();
    }

}
