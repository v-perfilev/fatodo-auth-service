package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class CaptchaFallbackFactory implements FallbackFactory<CaptchaClient> {
    @Override
    public CaptchaClient create(Throwable throwable) {
        return captchaRequestDTO -> {
            throw new ClientException();
        };
    }
}
