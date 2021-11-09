package com.csgomarket.csgomarketapi.security.userdetails;

import com.csgomarket.csgomarketapi.model.user.UserSettings;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Data
@Builder
public class UserDetailsImpl implements UserDetails {

    private String id;

    private String username;

    @JsonIgnore
    private String password;

    private BigDecimal cash;

    private UserSettings settings;

    private List<String> ownedItems;

    private final Collection<? extends GrantedAuthority> authorities = null;

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
