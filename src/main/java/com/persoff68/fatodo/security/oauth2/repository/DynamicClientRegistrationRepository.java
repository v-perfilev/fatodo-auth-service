package com.persoff68.fatodo.security.oauth2.repository;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.security.oauth2.clientregistration.AppleClientRegistrationFactory;
import com.persoff68.fatodo.security.oauth2.clientregistration.FacebookClientRegistrationFactory;
import com.persoff68.fatodo.security.oauth2.clientregistration.GoogleClientRegistrationFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DynamicClientRegistrationRepository implements ClientRegistrationRepository {

    private final GoogleClientRegistrationFactory googleClientRegistrationFactory;
    private final FacebookClientRegistrationFactory facebookClientRegistrationFactory;
    private final AppleClientRegistrationFactory appleClientRegistrationFactory;

    private final Map<String, ClientRegistration> registrationMap = new HashMap<>();

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return this.registrationMap.get(registrationId);
    }

    public void refreshRegistrationMap() {
        String googleProviderId = Provider.GOOGLE.getValue().toLowerCase();
        ClientRegistration googleClientRegistration = googleClientRegistrationFactory.create();
        String facebookProviderId = Provider.FACEBOOK.getValue().toLowerCase();
        ClientRegistration facebookClientRegistration = facebookClientRegistrationFactory.create();
        String appleProviderId = Provider.APPLE.getValue().toLowerCase();
        ClientRegistration appleClientRegistration = appleClientRegistrationFactory.create();

        registrationMap.clear();
        registrationMap.put(googleProviderId, googleClientRegistration);
        registrationMap.put(facebookProviderId, facebookClientRegistration);
        registrationMap.put(appleProviderId, appleClientRegistration);
    }

}
