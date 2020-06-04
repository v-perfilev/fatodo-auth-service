package com.persoff68.fatodo.security.local;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.security.exception.AuthWrongPasswordException;
import com.persoff68.fatodo.service.LocalUserDetailsService;
import com.persoff68.fatodo.service.exception.UserNotActivatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final LocalUserDetailsService localUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String emailOrUsername = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserPrincipal userPrincipal = localUserDetailsService.loadUser(emailOrUsername);

        if (!passwordEncoder.matches(password, userPrincipal.getPassword())) {
            throw new AuthWrongPasswordException();
        }

        if (!userPrincipal.isActivated()) {
            throw new UserNotActivatedException();
        }

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
