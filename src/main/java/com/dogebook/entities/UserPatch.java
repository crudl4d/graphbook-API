package com.dogebook.entities;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Getter
public class UserPatch {
    private String firstName;
    private String surname;
    private String email;
    private String password;
    private Date birthDate;

    public User toUser() {
        return User.builder()
                .firstName(getFirstName())
                .surname(getSurname())
                .email(getEmail())
                .password(!ObjectUtils.isEmpty(getPassword()) ? new BCryptPasswordEncoder().encode(getPassword()) : null)
                .birthDate(getBirthDate())
                .build();
    }
}
