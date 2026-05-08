package ru.isgaij.smartcloset.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class WishlistItemRequest {

    private String name;
    private BigDecimal price;
    private String url;
    private String note;
}