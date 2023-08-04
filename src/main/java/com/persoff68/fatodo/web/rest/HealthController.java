package com.persoff68.fatodo.web.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(HealthController.ENDPOINT)
@RequiredArgsConstructor
public class HealthController {
    static final String ENDPOINT = "/api/health";

    @GetMapping
    public ResponseEntity<Void> check() {
        return ResponseEntity.ok().build();
    }

}
