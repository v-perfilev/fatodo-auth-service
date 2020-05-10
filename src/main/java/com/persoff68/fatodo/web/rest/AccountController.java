package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.vm.ResetPasswordVM;
import com.persoff68.fatodo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(AccountController.ENDPOINT)
@RequiredArgsConstructor
public class AccountController {
    static final String ENDPOINT = "/api/account";

    private final AccountService accountService;

    @GetMapping(value = "/activate/{code}")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        accountService.activate(code);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/send-activation-code/{user}")
    public ResponseEntity<Void> sendActivationCode(@PathVariable String user) {
        accountService.sendActivationCodeMailByEmailOrUsername(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordVM resetPasswordVM) {
        accountService.resetPassword(resetPasswordVM);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/send-reset-password-code/{user}")
    public ResponseEntity<Void> sendResetPasswordCode(@PathVariable String user) {
        accountService.sendResetPasswordMail(user);
        return ResponseEntity.ok().build();
    }

}
