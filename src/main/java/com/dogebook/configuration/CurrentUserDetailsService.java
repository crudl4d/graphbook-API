package com.dogebook.configuration;

import com.dogebook.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CurrentUserDetailsService implements UserDetailsService {

    public CurrentUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        com.dogebook.entities.User user = userRepository.findByEmail(username);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRole());
        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(com.dogebook.entities.User user,
                                            List<GrantedAuthority> authorities) {
        String username = user.getEmail();
        String password = user.getPassword();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        UserContext userContext = new UserContext(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
        userContext.setId(user.getId());
        userContext.setEmail(user.getEmail());
        userContext.setName(user.getFirstName());
        return userContext;
    }

    private List<GrantedAuthority> buildUserAuthority(Set<String> roles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();
        for (String role : roles) {
            setAuths.add(new SimpleGrantedAuthority(role));
        }

        return new ArrayList<>(setAuths);
    }
}
