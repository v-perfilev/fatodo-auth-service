package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.vm.ForgotPasswordVM;
import com.persoff68.fatodo.model.vm.ResetPasswordVM;
import com.persoff68.fatodo.service.AccountService;
import com.persoff68.fatodo.service.client.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(AccountController.ENDPOINT)
@RequiredArgsConstructor
public class AccountController {
    static final String ENDPOINT = "/api/account";

    private final AccountService accountService;
    private final CaptchaService captchaService;

    @GetMapping("/activate/{code}")
    public ResponseEntity<Void> activate(@PathVariable("code") UUID code) {
        accountService.activate(code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/request-activation-code/{user}")
    public ResponseEntity<Void> requestActivationCode(@PathVariable String user) {
        accountService.sendActivationCodeMail(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordVM resetPasswordVM) {
        captchaService.captchaCheck(resetPasswordVM.getToken());
        accountService.resetPassword(resetPasswordVM);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request-reset-password-code")
    public ResponseEntity<Void> requestResetPasswordCode(@Valid @RequestBody ForgotPasswordVM forgotPasswordVM) {
        captchaService.captchaCheck(forgotPasswordVM.getToken());
        accountService.sendResetPasswordMail(forgotPasswordVM.getUser());
        return ResponseEntity.ok().build();
    }

}
