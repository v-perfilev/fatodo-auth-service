package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.security.oauth2.userinfo.OAuth2UserInfo;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserPrincipal userDTOToUserPrincipal(UserDTO userDTO);

    LocalUserDTO registerVMToLocalUserDTO(RegisterVM registerVM);

    @Mapping(source = "email", target = "username")
    @Mapping(source = "id", target = "providerId")
    @Mapping(target = "id", ignore = true)
    OAuth2UserDTO oAuth2UserInfoToOAuth2UserDTO(OAuth2UserInfo oAuth2UserInfo);

}
