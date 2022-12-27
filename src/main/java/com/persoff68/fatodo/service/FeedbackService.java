package com.persoff68.fatodo.service;

import com.persoff68.fatodo.service.client.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final MailService mailService;

    public void sendFeedback(String name, String email, String message) {
        mailService.sendFeedback(name, email, message);
    }

}
