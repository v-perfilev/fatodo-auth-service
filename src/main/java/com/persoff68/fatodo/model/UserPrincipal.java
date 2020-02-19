package com.persoff68.fatodo.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserPrincipal implements UserDetails, OAuth2User {
    private static final long serialVersionUID = 1L;

    private String id;
    private String email;
    private String username;
    private String password;
    private String provider;
    private String providerId;
    private Set<String> authorities;
    private Map<String, Object> attributes;

    @Override
    public String getName() {
        return email;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities != null
                ? authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
