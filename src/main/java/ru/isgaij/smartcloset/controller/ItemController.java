package ru.isgaij.smartcloset.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.isgaij.smartcloset.entity.Item;
import ru.isgaij.smartcloset.repository.BrandRepository;
import ru.isgaij.smartcloset.repository.CategoryRepository;
import ru.isgaij.smartcloset.repository.ItemRepository;
import ru.isgaij.smartcloset.service.ItemService;

@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ItemController(ItemService itemService, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.itemService = itemService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", itemService.findAll());
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
    public String save(@ModelAttribute Item item) {
        itemService.save(item);
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        model.addAttribute("item", item);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "item/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        itemService.deleteById(id);
        return "redirect:/items";
    }
}
