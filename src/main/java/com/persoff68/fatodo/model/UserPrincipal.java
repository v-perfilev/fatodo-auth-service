package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.config.constant.Provider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserPrincipal extends AbstractModel implements UserDetails, OAuth2User {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String id;
    private String email;
    private String username;
    private String password;
    private Provider provider;
    private String providerId;
    private Set<? extends GrantedAuthority> authorities;
    private transient Map<String, Object> attributes;

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
