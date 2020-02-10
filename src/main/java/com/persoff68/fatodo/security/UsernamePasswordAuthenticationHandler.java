package com.persoff68.fatodo.security;

import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.security.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationHandler extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    private void postConstruct() {
        setAuthenticationManager(this.authenticationManager);
        setFilterProcessesUrl("/**");
    }

    public UsernamePasswordAuthenticationHandler setUrl(String url) {
        setFilterProcessesUrl(url);
        return this;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var username = request.getParameter("username");
        var password = request.getParameter("password");
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        String token = jwtTokenProvider.createToken(authentication);
        ResponseUtils.addTokenToResponse(response, token);
    }


}
