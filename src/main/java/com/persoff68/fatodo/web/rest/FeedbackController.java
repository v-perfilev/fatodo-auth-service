package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.vm.FeedbackVM;
import com.persoff68.fatodo.service.FeedbackService;
import com.persoff68.fatodo.service.client.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(FeedbackController.ENDPOINT)
@RequiredArgsConstructor
public class FeedbackController {
    static final String ENDPOINT = "/api/feedback";

    private final FeedbackService feedbackService;
    private final CaptchaService captchaService;

    @PostMapping
    public ResponseEntity<Void> sendFeedback(@Valid @RequestBody FeedbackVM feedbackVM) {
        captchaService.captchaCheck(feedbackVM.getToken());
        feedbackService.sendFeedback(feedbackVM.getName(), feedbackVM.getEmail(), feedbackVM.getMessage());
        return ResponseEntity.ok().build();
    }

}
