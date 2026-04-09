package ru.isgaij.smartcloset.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WishlistController {

    @GetMapping("/wishlist")
    public String wishlist() {
        return "wishlist/index";
    }
}