package com.persoff68.fatodo.security.oauth2.clientregistration;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.io.IOException;

public interface ClientRegistrationFactory {

    ClientRegistration create() throws IOException;

}
