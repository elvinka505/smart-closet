package ru.isgaij.smartcloset.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isgaij.smartcloset.entity.WishlistItem;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class WishlistItemResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private String url;
    private String note;
    private LocalDate addedDate;

    public static WishlistItemResponse from(WishlistItem item) {
        WishlistItemResponse dto = new WishlistItemResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setUrl(item.getUrl());
        dto.setNote(item.getNote());
        dto.setAddedDate(item.getAddedDate());
        return dto;
    }
}