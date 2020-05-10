package com.persoff68.fatodo.model.mapper;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.RegisterVM;
import com.persoff68.fatodo.security.oauth2.userinfo.OAuth2UserInfo;
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

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringsToGrantedAuthorities")
    @Mapping(source = "provider", target = "provider", qualifiedByName = "stringToProvider")
    UserPrincipal userPrincipalDTOToUserPrincipal(UserPrincipalDTO userPrincipalDTO);

    LocalUserDTO registerVMToLocalUserDTO(RegisterVM registerVM);

    @Mapping(source = "email", target = "username")
    @Mapping(source = "id", target = "providerId")
    OAuth2UserDTO oAuth2UserInfoToOAuth2UserDTO(OAuth2UserInfo oAuth2UserInfo);

    @Named("stringsToGrantedAuthorities")
    default Set<GrantedAuthority> stringsToAuthorities(Set<String> stringSet) {
        return stringSet != null
                ? stringSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
                : null;
    }

    @Named("stringToProvider")
    default Provider stringToProvider(String provider) {
        return Provider.valueOf(provider);
    }

}
