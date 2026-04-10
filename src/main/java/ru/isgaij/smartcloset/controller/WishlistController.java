package ru.isgaij.smartcloset.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.isgaij.smartcloset.service.ExchangeRateService;

@Controller
public class WishlistController {

    @GetMapping("/wishlist")
    public String wishlist(Model model) {
        model.addAttribute("rates", exchangeRateService.getSelectedRates());
        return "wishlist/index";
    }

    private final ExchangeRateService exchangeRateService;
    public WishlistController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

}