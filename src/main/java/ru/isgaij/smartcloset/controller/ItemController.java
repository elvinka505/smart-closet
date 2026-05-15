package ru.isgaij.smartcloset.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.isgaij.smartcloset.dto.ItemForm;
import ru.isgaij.smartcloset.entity.Item;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.exception.ResourceNotFoundException;
import ru.isgaij.smartcloset.repository.BrandRepository;
import ru.isgaij.smartcloset.repository.CategoryRepository;
import ru.isgaij.smartcloset.repository.UserRepository;
import ru.isgaij.smartcloset.service.ColorService;
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
    private final ColorService colorService;

    public ItemController(ItemService itemService, CategoryRepository categoryRepository, BrandRepository brandRepository, UserRepository userRepository, ColorService colorService) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.colorService = colorService;
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

        Map<String, String> colorNames = new HashMap<>();
        for (Item item : items) {
            if (item.getColor() != null && !item.getColor().isEmpty()) {
                String hex = item.getColor().replace("#", "");
                Map<String, String> info = colorService.getColorInfo(hex);
                colorNames.put(String.valueOf(item.getId()), info.getOrDefault("name", item.getColor()));
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("matchingItems", matchingItems);
        model.addAttribute("colorNames", colorNames);

        return "item/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("form", new ItemForm());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "item/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("form") ItemForm form,
                       BindingResult bindingResult,
                       Principal principal,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("brands", brandRepository.findAll());
            return "item/form";
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item item = itemService.fromForm(form, user);
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

        ItemForm form = itemService.toForm(item);
        model.addAttribute("form", form);
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
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ItemForm form,
                         BindingResult bindingResult,
                         Principal principal,
                         Model model) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Item existingItem = itemService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (!existingItem.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Item not found");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("brands", brandRepository.findAll());
            return "item/form";
        }

        form.setId(id);
        Item item = itemService.fromForm(form, user);
        itemService.save(item);
        return "redirect:/items";
    }
}
