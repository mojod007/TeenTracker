package com.trace.product.controller;

import com.trace.product.entity.Gamme;
import com.trace.product.service.GammeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gammes")
@RequiredArgsConstructor
public class GammeWebController {
    private final GammeService gammeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("gammes", gammeService.getAllGammes());
        return "gamme-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("gamme", new Gamme());
        return "gamme-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("gamme",
                gammeService.getGammeById(id).orElseThrow(() -> new RuntimeException("Gamme not found")));
        return "gamme-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Gamme gamme) {
        gammeService.saveGamme(gamme);
        return "redirect:/gammes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        gammeService.deleteGamme(id);
        return "redirect:/gammes";
    }
}
