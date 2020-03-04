package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.security.oauth2.userinfo.OAuth2UserInfo;
import com.persoff68.fatodo.web.rest.vm.RegisterVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringsIntoGrantedAuthorities")
    UserPrincipal userPrincipalDTOToUserPrincipal(UserPrincipalDTO userPrincipalDTO);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringsIntoGrantedAuthorities")
    UserPrincipal userDTOToUserPrincipal(UserDTO userDTO);

    LocalUserDTO registerVMToLocalUserDTO(RegisterVM registerVM);

    @Mapping(source = "email", target = "username")
    @Mapping(source = "id", target = "providerId")
    @Mapping(target = "id", ignore = true)
    OAuth2UserDTO oAuth2UserInfoToOAuth2UserDTO(OAuth2UserInfo oAuth2UserInfo);

    @Named("stringsIntoGrantedAuthorities")
    default Set<? extends GrantedAuthority> stringsIntoAuthorities(Set<String> stringSet) {
        return stringSet != null
                ? stringSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
                : null;
    }

}
