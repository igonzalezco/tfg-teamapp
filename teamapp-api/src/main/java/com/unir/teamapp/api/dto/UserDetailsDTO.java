package com.unir.teamapp.api.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserDetailsDTO extends User {

    private static final long serialVersionUID = -1234567899546846654L;

    private Integer userId;

    public UserDetailsDTO(final String username, final String password, final Collection<? extends GrantedAuthority> roles, final Integer userId) {
        super(username, password, true, true, true, true, roles);
        setUserId(userId);
    }

}
