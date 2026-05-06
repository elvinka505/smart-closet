package ru.isgaij.smartcloset.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isgaij.smartcloset.entity.Item;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.exception.ResourceNotFoundException;
import ru.isgaij.smartcloset.repository.BrandRepository;
import ru.isgaij.smartcloset.repository.CategoryRepository;
import ru.isgaij.smartcloset.repository.UserRepository;
import ru.isgaij.smartcloset.service.ItemService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public ItemController(ItemService itemService, CategoryRepository categoryRepository, BrandRepository brandRepository, UserRepository userRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Item> items = itemService.findAllByUser(user);

        Map<String, List<Item>> matchingItems = new HashMap<>();
        for (Item item : items) {
            matchingItems.put(String.valueOf(item.getId()), itemService.findMatchingItems(item));
        }

        model.addAttribute("items", items);
        model.addAttribute("matchingItems", matchingItems);

        return "item/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "item/form";
    }

    @PostMapping
    public String save(@ModelAttribute Item item, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        item.setUser(user);
        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item item = itemService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Item not found");
        }

        model.addAttribute("item", item);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "item/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item item = itemService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Item not found");
        }

        itemService.deleteById(id);
        return "redirect:/items";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Item item, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item existingItem = itemService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!existingItem.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Item not found");
        }

        item.setId(id);
        item.setUser(user);
        itemService.save(item);
        return "redirect:/items";
    }
}
