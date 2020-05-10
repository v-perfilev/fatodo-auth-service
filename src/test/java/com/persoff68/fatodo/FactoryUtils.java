package com.persoff68.fatodo;

import com.persoff68.fatodo.model.Activation;
import com.persoff68.fatodo.model.dto.ActivationDTO;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.LoginVM;
import com.persoff68.fatodo.model.vm.RegisterVM;
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
        attributeMap.put("id", "test_id_" + postfix);
        attributeMap.put("name", "test_name" + postfix);
        attributeMap.put("email", "test_" + postfix + "@email.com");
        String nameAttributeKey = "id";
        return new DefaultOAuth2User(authorityList, attributeMap, nameAttributeKey);
    }

    public static LocalUserDTO createLocalUserDTO(String postfix, String password) {
        LocalUserDTO dto = new LocalUserDTO();
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setPassword(password);
        dto.setLanguage("en");
        return dto;
    }

    public static OAuth2UserDTO createOAuth2UserDTO(String postfix) {
        OAuth2UserDTO dto = new OAuth2UserDTO();
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_" + postfix + "@email.com");
        dto.setProvider("facebook");
        dto.setProviderId("test_provider_id");
        dto.setLanguage("en");
        return dto;
    }

    public static LoginVM createUsernameLoginVM(String postfix, String password) {
        LoginVM vm = new LoginVM();
        vm.setUser("test_username_" + postfix);
        vm.setPassword(password);
        return vm;
    }

    public static LoginVM createEmailLoginVM(String postfix, String password) {
        LoginVM vm = new LoginVM();
        vm.setUser("test_" + postfix + "@email.com");
        vm.setPassword(password);
        return vm;
    }

    public static LoginVM createInvalidLoginVM() {
        return new LoginVM();
    }

    public static RegisterVM createRegisterVM(String postfix, String password) {
        RegisterVM vm = new RegisterVM();
        vm.setEmail("test_" + postfix + "@email.com");
        vm.setUsername("test_username_" + postfix);
        vm.setPassword(password);
        vm.setLanguage("en");
        return vm;
    }

    public static RegisterVM createInvalidRegisterVM() {
        return new RegisterVM();
    }

    public static UserPrincipalDTO createUserPrincipalDTO(String postfix, String provider, String password) {
        Set<String> authorityList = Collections.singleton("ROLE_USER");
        UserPrincipalDTO dto = new UserPrincipalDTO();
        dto.setId("test_id_" + postfix);
        dto.setEmail("test_" + postfix + "@email.com");
        dto.setUsername("test_username_" + postfix);
        dto.setPassword(password);
        dto.setProvider(provider);
        dto.setAuthorities(authorityList);
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

    public static Activation createActivation(String code, boolean activated) {
        Activation activation = new Activation();
        activation.setUserId(UUID.randomUUID().toString());
        activation.setCode(code);
        activation.setActivated(activated);
        return activation;
    }

    public static ActivationDTO createActivationDTO() {
        ActivationDTO dto = new ActivationDTO();
        dto.setLanguage("en");
        dto.setEmail("test@email.com");
        dto.setUsername("test_user");
        dto.setCode(UUID.randomUUID().toString());
        return dto;
    }

}
