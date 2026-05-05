package ru.isgaij.smartcloset.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.isgaij.smartcloset.service.ColorService;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ColorRestController {

    private final ColorService colorService;

    public ColorRestController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/color")
    public Map<String, String> getColorInfo(@RequestParam String hex) {
        return colorService.getColorInfo(hex);
    }
}