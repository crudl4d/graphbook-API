package com.dogebook.configuration;

import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CurrentUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        //Try to find user and its roles, for example here we try to get it from database via a DAO object
        //Do not confuse this foo.bar.User with CurrentUser or spring User, this is a temporary object which holds user info stored in database
        com.dogebook.entities.User user = userRepository.findByEmail(username);

        //Build user Authority. some how a convert from your custom roles which are in database to spring GrantedAuthority
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRole());

        //The magic is happen in this private method !
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
        userContext.setName(user.getName());
        return userContext;
    }

    private List<GrantedAuthority> buildUserAuthority(Set<String> roles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (String role : roles) {
            setAuths.add(new SimpleGrantedAuthority(role));
        }

        return new ArrayList<GrantedAuthority>(setAuths);
    }
}
