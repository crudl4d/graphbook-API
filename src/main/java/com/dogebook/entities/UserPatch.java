package com.dogebook.entities;

import lombok.Getter;

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
                .password(getPassword())
                .birthDate(getBirthDate())
                .build();
    }
}
