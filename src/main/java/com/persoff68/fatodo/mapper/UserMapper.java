package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserPrincipal userDTOToUserPrincipal(UserDTO userDTO);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "grantedAuthoritiesIntoStrings")
    UserDTO userPrincipalToUserDTO(UserPrincipal userPrincipal);

    static Set<String> grantedAuthoritiesIntoStrings(List<? extends GrantedAuthority> grantedAuthorityList) {
        return grantedAuthorityList != null
                ? grantedAuthorityList.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
                : null;
    }
}
