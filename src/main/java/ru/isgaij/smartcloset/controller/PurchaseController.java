package ru.isgaij.smartcloset.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.isgaij.smartcloset.entity.Purchase;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.exception.ResourceNotFoundException;
import ru.isgaij.smartcloset.service.ItemService;
import ru.isgaij.smartcloset.service.PurchaseService;
import ru.isgaij.smartcloset.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final UserService userService;
    private final ItemService itemService;

    public PurchaseController(PurchaseService purchaseService, UserService userService, ItemService itemService) {
        this.purchaseService = purchaseService;
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping
    public String purchase(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        model.addAttribute("purchases", purchaseService.findAllByUserId(user.getId()));
        return "purchase/list";
    }

    @GetMapping("/new")
    public String newPurchase(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        model.addAttribute("purchase", new Purchase());
        model.addAttribute("items", itemService.findAllByUser(user));

        return "purchase/form";
    }

    @PostMapping
    public String savePurchase(@ModelAttribute Purchase purchase, Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        purchase.setUser(user);
        purchaseService.save(purchase);

        return "redirect:/purchases";
    }

}