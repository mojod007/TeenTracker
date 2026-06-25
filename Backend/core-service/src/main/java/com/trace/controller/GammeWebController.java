package com.trace.controller;

import com.trace.entity.Gamme;
import com.trace.service.GammeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gammes")
@RequiredArgsConstructor
public class GammeWebController {

    private final GammeService gammeService;

    @PreAuthorize("hasAuthority('GAMME_VIEW')")
    @GetMapping
    public String list(Model model) {
        model.addAttribute("gammes", gammeService.getAllGammes());
        return "gamme-list";
    }

    @PreAuthorize("hasAuthority('GAMME_CREATE')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("gamme", new Gamme());
        return "gamme-form";
    }

    @PreAuthorize("hasAuthority('GAMME_UPDATE')")
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("gamme",
                gammeService.getGammeById(id).orElseThrow(() -> new RuntimeException("Gamme not found")));
        return "gamme-form";
    }

    @PreAuthorize("hasAuthority('GAMME_CREATE') or hasAuthority('GAMME_UPDATE')")
    @PostMapping("/save")
    public String save(@ModelAttribute Gamme gamme) {
        gammeService.saveGamme(gamme);
        return "redirect:/gammes";
    }

    @PreAuthorize("hasAuthority('GAMME_DELETE')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        gammeService.deleteGamme(id);
        return "redirect:/gammes";
    }
}
