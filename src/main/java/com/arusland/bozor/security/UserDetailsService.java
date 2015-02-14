package com.arusland.bozor.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Value("${users.admin.password}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        if (SecurityConstants.ADMIN_USER_NAME.equals(lowercaseLogin)) {
            List<GrantedAuthority> grantedAuthorities = new LinkedList<>();

            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    adminPassword,
                    grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User '" + login + "' was not found in the database");
        }
    }
}
