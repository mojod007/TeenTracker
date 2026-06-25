package com.trace.controller;

import com.trace.entity.User;
import com.trace.service.EtablissementService;
import com.trace.service.ProfileService;
import com.trace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;
    private final EtablissementService etablissementService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("profiles", profileService.findAll());
        model.addAttribute("allEtablissements", etablissementService.getAllEtablissementsWithDepots());
        model.addAttribute("allDepots", etablissementService.getAllDepots());
        model.addAttribute("assignedEtabIds", new java.util.ArrayList<Long>());
        model.addAttribute("assignedDepotIds", new java.util.ArrayList<Long>());
        return "user-form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("profiles", profileService.findAll());
        model.addAttribute("allEtablissements", etablissementService.getAllEtablissementsWithDepots());
        model.addAttribute("allDepots", etablissementService.getAllDepots());

        Map<String, Object> assignments = etablissementService.getUserAssignments(id);
        model.addAttribute("assignedEtabIds",
                assignments.getOrDefault("etablissementIds", new java.util.ArrayList<>()));
        model.addAttribute("assignedDepotIds", assignments.getOrDefault("depotIds", new java.util.ArrayList<>()));
        model.addAttribute("userEtablissements", assignments.get("etablissements"));
        model.addAttribute("userDepots", assignments.get("depots"));

        return "user-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('USER_CREATE') or hasAuthority('USER_UPDATE')")
    public String save(@ModelAttribute User user,
            @RequestParam(value = "etablissementIds", required = false) List<Long> etablissementIds,
            @RequestParam(value = "depotIds", required = false) List<Long> depotIds) {
        User savedUser = userService.save(user);
        etablissementService.saveUserAssignments(savedUser.getId(), etablissementIds, depotIds);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/toggle-active/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public String toggleActive(@PathVariable Long id) {
        userService.toggleActive(id);
        return "redirect:/users";
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allEtablissements", etablissementService.getAllEtablissementsWithDepots());

        Map<String, Object> assignments = etablissementService.getUserAssignments(id);
        model.addAttribute("assignedEtabIds", assignments.get("etablissementIds"));
        model.addAttribute("assignedDepotIds", assignments.get("depotIds"));

        return "user-details";
    }
}
