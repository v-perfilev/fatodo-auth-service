package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
