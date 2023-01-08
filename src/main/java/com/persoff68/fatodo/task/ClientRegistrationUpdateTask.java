package com.persoff68.fatodo.task;

import com.persoff68.fatodo.security.oauth2.repository.DynamicClientRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class ClientRegistrationUpdateTask {
    private static final long CLIENT_REGISTRATION_TTL = 86400L * 1000L * 30L; // 30 days

    private final DynamicClientRegistrationRepository dynamicClientRegistrationRepository;

    @Scheduled(fixedDelay = CLIENT_REGISTRATION_TTL)
    public void update() {
        dynamicClientRegistrationRepository.refreshRegistrationMap();
    }

}
