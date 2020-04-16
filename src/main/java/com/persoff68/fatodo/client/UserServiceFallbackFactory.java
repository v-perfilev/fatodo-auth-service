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
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceClient> {
    @Override
    public UserServiceClient create(Throwable throwable) {
        int status = throwable instanceof FeignException
                ? ((FeignException) throwable).status()
                : 0;

        return new UserServiceClient() {
            @Override
            public UserPrincipalDTO getUserPrincipalByUsername(String username) {
                if (status == 404) {
                    throw new ModelNotFoundException();
                }
                throw new ClientException();
            }

            @Override
            public UserPrincipalDTO getUserPrincipalByEmail(String email) {
                if (status == 404) {
                    throw new ModelNotFoundException();
                }
                throw new ClientException();
            }

            @Override
            public UserDTO createOAuth2User(OAuth2UserDTO oAuth2UserDTO) {
                if (status == 400) {
                    throw new ModelAlreadyExistsException();
                }
                if (status == 409) {
                    throw new ModelDuplicatedException();
                }
                throw new ClientException();
            }

            @Override
            public UserDTO createLocalUser(LocalUserDTO userLocalDTO) {
                if (status == 400) {
                    throw new ModelAlreadyExistsException();
                }
                if (status == 409) {
                    throw new ModelDuplicatedException();
                }
                throw new ClientException();
            }
        };

    }
}
