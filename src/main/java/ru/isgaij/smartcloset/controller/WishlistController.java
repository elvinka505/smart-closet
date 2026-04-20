package ru.isgaij.smartcloset.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.isgaij.smartcloset.service.ColorService;

@Controller
public class WishlistController {

    private final ColorService colorService;

    public WishlistController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/wishlist")
    public String wishlist(@RequestParam(defaultValue = "FFFFFF") String hex, Model model) {
        model.addAttribute("colorInfo", colorService.getColorInfo(hex));
        model.addAttribute("hex", hex);
        return "wishlist/index";
    }
}