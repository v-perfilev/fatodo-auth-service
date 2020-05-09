package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.service.ActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ActivationController.ENDPOINT)
@RequiredArgsConstructor
public class ActivationController {
    static final String ENDPOINT = "/api/activation";

    private final ActivationService activationService;

    @GetMapping(value = "/activate/{code}")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        activationService.activate(code);
        return ResponseEntity.ok().build();
    }

}
