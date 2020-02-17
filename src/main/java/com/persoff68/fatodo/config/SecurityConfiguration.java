package com.persoff68.fatodo.config;

import com.persoff68.fatodo.security.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.security.OAuth2AuthenticationFailureHandler;
import com.persoff68.fatodo.security.OAuth2AuthenticationSuccessHandler;
import com.persoff68.fatodo.security.UsernamePasswordAuthenticationHandler;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.security.service.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;
    private final OAuth2UserService OAuth2UserService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder authManager) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        authManager.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public UsernamePasswordAuthenticationHandler authenticationHandler() throws Exception {
        return new UsernamePasswordAuthenticationHandler(appProperties, authenticationManager(), jwtTokenProvider);
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(appProperties, jwtTokenProvider, cookieAuthorizationRequestRepository);
    }

    @Bean
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(cookieAuthorizationRequestRepository);
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
                .userService(OAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler())
                .and()
                .addFilter(authenticationHandler().setUrl("/authenticate"));
    }

}
