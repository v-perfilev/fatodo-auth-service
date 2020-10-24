package com.persoff68.fatodo;

import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.ResetPassword;
import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.web.rest.vm.ForgotPasswordVM;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import com.persoff68.fatodo.web.rest.vm.ResetPasswordVM;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FactoryUtils {

    public static OAuth2User createOAuth2User(String postfix) {
        List<? extends GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("id", UUID.randomUUID());
        attributeMap.put("name", "test_name" + postfix);
        attributeMap.put("email", "test_" + postfix + "@email.com");
        String nameAttributeKey = "id";
        return new DefaultOAuth2User(authorityList, attributeMap, nameAttributeKey);
    }

    public static RegisterVM createRegisterVM(String postfix, String password) {
        RegisterVM vm = new RegisterVM();
        vm.setEmail("test_" + postfix + "@email.com");
        vm.setUsername("test_username_" + postfix);
        vm.setPassword(password);
        vm.setLanguage("en");
        vm.setToken("test_token");
        return vm;
    }

    public static RegisterVM createInvalidRegisterVM() {
        return new RegisterVM();
    }

    public static UserPrincipalDTO createUserPrincipalDTO(String postfix, String provider, String password) {
        return createUserPrincipalDTO(postfix, provider, password, true);
    }

    public static UserPrincipalDTO createUserPrincipalDTO(String postfix, String provider,
                                                          String password, boolean isActivated) {
        Set<String> authorityList = Collections.singleton("ROLE_USER");
        UserPrincipalDTO dto = new UserPrincipalDTO();
        dto.setId(UUID.randomUUID());
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setPassword(password);
        dto.setProvider(provider);
        dto.setAuthorities(authorityList);
        dto.setActivated(isActivated);
        return dto;
    }

    public static OAuth2UserRequest createUserRequest(String provider) {
        ClientRegistration clientRegistration = CommonOAuth2Provider.valueOf(provider).getBuilder(provider)
                .clientId("test_client_id")
                .clientSecret("test_client_secret")
                .build();
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "test_token_value",
                Instant.now(),
                Instant.MAX);
        return new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
    }

    public static Activation createActivation(UUID userId, UUID code, boolean completed) {
        Activation activation = new Activation();
        activation.setUserId(userId);
        activation.setCode(code);
        activation.setCompleted(completed);
        return activation;
    }

    public static ActivationMailDTO createActivationMailDTO() {
        ActivationMailDTO dto = new ActivationMailDTO();
        dto.setLanguage("en");
        dto.setEmail("test@email.com");
        dto.setUsername("test_user");
        dto.setCode(UUID.randomUUID());
        return dto;
    }

    public static ResetPassword createResetPassword(String userId, String code, boolean completed) {
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setCode(code);
        resetPassword.setUserId(UUID.randomUUID());
        resetPassword.setCompleted(completed);
        return resetPassword;
    }

    public static ResetPasswordVM createResetPasswordVM(String code) {
        ResetPasswordVM vm = new ResetPasswordVM();
        vm.setCode(code);
        vm.setPassword("test_password");
        vm.setToken("test_token");
        return vm;
    }

    public static ResetPasswordDTO createResetPasswordDTO() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setUserId(UUID.randomUUID());
        dto.setPassword("test_password");
        return dto;
    }

    public static ResetPasswordMailDTO createResetPasswordMailDTO() {
        ResetPasswordMailDTO dto = new ResetPasswordMailDTO();
        dto.setLanguage("en");
        dto.setEmail("test@email.com");
        dto.setUsername("test_user");
        dto.setCode(UUID.randomUUID().toString());
        return dto;
    }
//
//    public static CaptchaResponseDTO createCaptchaResponseDTO(boolean success) {
//        CaptchaResponseDTO dto = new CaptchaResponseDTO();
//        dto.setSuccess(success);
//        return dto;
//    }

    public static ForgotPasswordVM createForgotPasswordVM(String user) {
        ForgotPasswordVM vm = new ForgotPasswordVM();
        vm.setUser(user);
        vm.setToken("test_token");
        return vm;
    }

}
