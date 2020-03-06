package com.persoff68.fatodo.oauth2;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.security.exception.AuthWrongProviderProblem;
import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.constant.AuthProvider;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.service.OAuth2UserDetailsService;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuth2UserDetailsServiceTest {

    OAuth2UserDetailsService oAuth2UserDetailsService;

    @Mock
    DefaultOAuth2UserService defaultOAuth2UserService;
    @Mock
    UserServiceClient userServiceClient;

    OAuth2UserRequest facebookUserRequest;
    OAuth2UserRequest googleUserRequest;
    UserDTO newFacebookUserDTO;
    UserDTO newGoogleUserDTO;
    UserPrincipalDTO existingFacebookUserPrincipalDTO;
    UserPrincipalDTO existingGoogleUserPrincipalDTO;

    @BeforeEach
    void setup() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        oAuth2UserDetailsService = new OAuth2UserDetailsService(userServiceClient, userMapper, defaultOAuth2UserService);

        facebookUserRequest = createUserRequest(AuthProvider.FACEBOOK.name());
        googleUserRequest = createUserRequest(AuthProvider.GOOGLE.name());
        newFacebookUserDTO = createNewUserDTO(AuthProvider.FACEBOOK);
        newGoogleUserDTO = createNewUserDTO(AuthProvider.GOOGLE);
        existingFacebookUserPrincipalDTO = createExistingUserPrincipalDTO(AuthProvider.FACEBOOK);
        existingGoogleUserPrincipalDTO = createExistingUserPrincipalDTO(AuthProvider.GOOGLE);

        when(defaultOAuth2UserService.loadUser(any())).thenReturn(createOAuth2User());
    }

    @Test
    void testProcessOAuth2User_google_userNotExist() {
        when(userServiceClient.getUserPrincipalByEmail(any())).thenThrow(FeignException.NotFound.class);
        when(userServiceClient.createOAuth2User(any())).thenReturn(newGoogleUserDTO);
        OAuth2User oAuth2User = oAuth2UserDetailsService.loadUser(googleUserRequest);
        assertThat(oAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userNotExist() {
        when(userServiceClient.getUserPrincipalByEmail(any())).thenThrow(FeignException.NotFound.class);
        when(userServiceClient.createOAuth2User(any())).thenReturn(newFacebookUserDTO);
        OAuth2User oAuth2User = oAuth2UserDetailsService.loadUser(facebookUserRequest);
        assertThat(oAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist() {
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(existingFacebookUserPrincipalDTO);
        OAuth2User oAuth2User = oAuth2UserDetailsService.loadUser(facebookUserRequest);
        assertThat(oAuth2User).isNotNull();
    }

    @Test
    void testProcessOAuth2User_facebook_userExist_wrongProvider() {
        when(userServiceClient.getUserPrincipalByEmail(any())).thenReturn(existingGoogleUserPrincipalDTO);
        assertThatThrownBy(() -> oAuth2UserDetailsService.loadUser(facebookUserRequest)).isInstanceOf(AuthWrongProviderProblem.class);
    }


    private static OAuth2UserRequest createUserRequest(String provider) {
        ClientRegistration clientRegistration = createClientRegistration(provider);
        OAuth2AccessToken oAuth2AccessToken = createTestOAuth2AccessToken();
        return new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
    }

    private static ClientRegistration createClientRegistration(String provider) {
        return CommonOAuth2Provider.FACEBOOK.getBuilder(provider)
                .clientId("test_client_id").clientSecret("test_client_secret").build();
    }

    private static OAuth2AccessToken createTestOAuth2AccessToken() {
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "test_token_value",
                Instant.now(),
                Instant.MAX);
    }

    private static OAuth2User createOAuth2User() {
        List<? extends GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("id", "test_id");
        attributeMap.put("name", "test_name");
        attributeMap.put("email", "test@email.com");
        String nameAttributeKey = "id";
        return new DefaultOAuth2User(authorityList, attributeMap, nameAttributeKey);
    }

    private static UserDTO createNewUserDTO(AuthProvider authProvider) {
        Set<String> authorityList = Collections.singleton("ROLE_USER");
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test_id");
        userDTO.setUsername("test@email.com");
        userDTO.setEmail("test@email.com");
        userDTO.setProvider(authProvider.name());
        userDTO.setAuthorities(authorityList);
        return userDTO;
    }

    private static UserPrincipalDTO createExistingUserPrincipalDTO(AuthProvider authProvider) {
        Set<String> authorityList = Collections.singleton("ROLE_USER");
        UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO();
        userPrincipalDTO.setId("test_id");
        userPrincipalDTO.setUsername("test@email.com");
        userPrincipalDTO.setEmail("test@email.com");
        userPrincipalDTO.setProvider(authProvider.name());
        userPrincipalDTO.setAuthorities(authorityList);
        return userPrincipalDTO;
    }
}
