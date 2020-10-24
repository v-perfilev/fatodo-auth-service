package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class MailServiceFallbackFactory implements FallbackFactory<MailServiceClient> {
    @Override
    public MailServiceClient create(Throwable throwable) {

        return new MailServiceClient() {
            @Override
            public void sendActivationCode(ActivationMailDTO activationMailDTO) {
                throw new ClientException();

            }

            @Override
            public void sendResetPasswordCode(ResetPasswordMailDTO resetPasswordMailDTO) {
                throw new ClientException();

            }
        };
    }
}
