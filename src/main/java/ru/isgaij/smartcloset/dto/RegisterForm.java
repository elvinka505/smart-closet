package ru.isgaij.smartcloset.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class RegisterForm {
    private String username;
    private String email;
    private String password;
}
