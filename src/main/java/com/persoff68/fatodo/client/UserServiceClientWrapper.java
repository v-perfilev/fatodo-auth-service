package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.service.exception.ModelAlreadyExistsException;
import com.persoff68.fatodo.service.exception.ModelDuplicatedException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class UserServiceClientWrapper implements UserServiceClient {

    @Qualifier("userServiceClient")
    private final UserServiceClient userServiceClient;

    @Override
    public UserPrincipalDTO getUserPrincipalByUsername(String username) {
        try {
            return userServiceClient.getUserPrincipalByUsername(username);
        } catch (FeignException.NotFound e) {
            throw new ModelNotFoundException();
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public UserPrincipalDTO getUserPrincipalByEmail(String email) {
        try {
            return userServiceClient.getUserPrincipalByEmail(email);
        } catch (FeignException.NotFound e) {
            throw new ModelNotFoundException();
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public UserDTO createOAuth2User(OAuth2UserDTO oAuth2UserDTO) {
        try {
            return userServiceClient.createOAuth2User(oAuth2UserDTO);
        } catch (FeignException.BadRequest e) {
            throw new ModelAlreadyExistsException();
        } catch (FeignException.Conflict e) {
            throw new ModelDuplicatedException();
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public UserDTO createLocalUser(LocalUserDTO userLocalDTO) {
        try {
            return userServiceClient.createLocalUser(userLocalDTO);
        } catch (FeignException.BadRequest e) {
            throw new ModelAlreadyExistsException();
        } catch (FeignException.Conflict e) {
            throw new ModelDuplicatedException();
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void activate(String userId) {
        try {
            userServiceClient.activate(userId);
        } catch (Exception e) {
            throw new ClientException();
        }
    }
}
