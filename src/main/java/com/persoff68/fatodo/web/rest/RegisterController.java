package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.service.CaptchaService;
import com.persoff68.fatodo.service.RegisterService;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    static final String ENDPOINT = "/api/register";

    private final RegisterService registerService;
    private final CaptchaService captchaService;
    private final UserMapper userMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerLocal(@Valid @RequestBody RegisterVM registerVM) {
        captchaService.captchaCheck(registerVM.getToken());
        LocalUserDTO localUserDTO = userMapper.registerVMToLocalUserDTO(registerVM);
        registerService.register(localUserDTO);
        return ResponseEntity.ok().build();
    }

}
