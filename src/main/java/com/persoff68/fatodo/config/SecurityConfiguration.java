package com.persoff68.fatodo.config;

import com.persoff68.fatodo.repository.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.security.OAuth2AuthenticationFailureHandler;
import com.persoff68.fatodo.security.OAuth2AuthenticationSuccessHandler;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.service.LocalUserDetailsService;
import com.persoff68.fatodo.service.OAuth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityProblemSupport securityProblemSupport;
    private final LocalUserDetailsService localUserDetailsService;
    private final OAuth2UserDetailsService OAuth2UserDetailsService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManager) throws Exception {
        authManager.userDetailsService(localUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/api/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                .redirectionEndpoint().baseUri("/api/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(OAuth2UserDetailsService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());
    }

    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(appProperties, jwtTokenProvider, cookieAuthorizationRequestRepository);
    }

    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(cookieAuthorizationRequestRepository);
    }

}
