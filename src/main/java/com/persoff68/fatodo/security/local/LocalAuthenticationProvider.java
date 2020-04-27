package com.persoff68.fatodo.security.local;

import com.persoff68.fatodo.security.exception.AuthWrongPasswordException;
import com.persoff68.fatodo.service.LocalUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final LocalUserDetailsService localUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String emailOrUsername = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userPrincipal = localUserDetailsService.loadUser(emailOrUsername);

        if (!passwordEncoder.matches(password, userPrincipal.getPassword())) {
            throw new AuthWrongPasswordException();
        }

        return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
