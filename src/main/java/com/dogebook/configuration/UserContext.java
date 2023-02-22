package com.dogebook.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Principal;
import java.util.Collection;

@Getter
@Setter
public class UserContext extends User {
    public UserContext(String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static UserContext getUser(Principal principal) {
        return ((UserContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
    }

    private Long id;
    private String email;
    private String name;

}
