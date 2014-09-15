package org.magnum.mobilecloud.video.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by pbastidas on 9/14/14.
 */
public class User implements UserDetails{

    public static UserDetails create(String username, String password, String... authorities){
        return new User(username, password, authorities);
    }

    private final Collection<GrantedAuthority> authorities_;
    private final String username_;
    private final String password_;

    private User(
              String username_
            , String password_
            , String... authorities_
    ) {
        this.authorities_ = AuthorityUtils.createAuthorityList(authorities_);
        this.username_ = username_;
        this.password_ = password_;
    }

    public User(Collection<GrantedAuthority> authorities_, String username_, String password_) {
        this.authorities_ = authorities_;
        this.username_ = username_;
        this.password_ = password_;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities_;
    }

    @Override
    public String getPassword() {
        return password_;
    }

    @Override
    public String getUsername() {
        return username_;
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
