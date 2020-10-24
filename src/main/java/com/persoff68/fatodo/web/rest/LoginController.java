package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.security.util.ResponseUtils;
import com.persoff68.fatodo.service.CaptchaService;
import com.persoff68.fatodo.web.rest.vm.LoginVM;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(LoginController.ENDPOINT)
@RequiredArgsConstructor
public class LoginController {
    static final String ENDPOINT = "/api/authenticate";

    private final AppProperties appProperties;
    private final CaptchaService captchaService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> authenticate(@Valid @RequestBody LoginVM loginVM) {
        captchaService.captchaCheck(loginVM.getToken());
        var authenticationToken
                = new UsernamePasswordAuthenticationToken(loginVM.getUser(), loginVM.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = new User(userPrincipal.getUsername(), "", userPrincipal.getAuthorities());
        String jwt = jwtTokenProvider.createUserJwt(userPrincipal.getId(), user);
        HttpHeaders headers = ResponseUtils.createHeaderWithJwt(appProperties.getAuth(), jwt);
        return ResponseEntity.ok().headers(headers).build();
    }

}
