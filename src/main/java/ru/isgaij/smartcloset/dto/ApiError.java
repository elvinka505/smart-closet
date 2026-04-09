package ru.isgaij.smartcloset.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ApiError {
    private String message;
    private String timestamp;
}
