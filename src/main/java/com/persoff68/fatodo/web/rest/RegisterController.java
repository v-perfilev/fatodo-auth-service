package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.vm.RegisterVM;
import com.persoff68.fatodo.service.RegisterService;
import com.persoff68.fatodo.service.client.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(RegisterController.ENDPOINT)
@RequiredArgsConstructor
public class RegisterController {
    static final String ENDPOINT = "/api/account";

    private final RegisterService registerService;
    private final CaptchaService captchaService;
    private final UserMapper userMapper;

    @PostMapping(value = "/register")
    public ResponseEntity<Void> registerLocal(@Valid @RequestBody RegisterVM registerVM) {
        captchaService.captchaCheck(registerVM.getToken());
        LocalUserDTO localUserDTO = userMapper.registerVMToLocalUserDTO(registerVM);
        registerService.register(localUserDTO);
        return ResponseEntity.ok().build();
    }

}
