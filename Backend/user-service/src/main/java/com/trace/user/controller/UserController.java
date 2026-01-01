package com.trace.user.controller;

import com.trace.user.entity.User;
import com.trace.user.service.ProfileService;
import com.trace.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProfileService profileService;
    private final org.springframework.web.client.RestTemplate restTemplate;

    private static final String ETAB_SERVICE_URL = "http://gateway-service/api/etablissements";

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("profiles", profileService.findAll());
        model.addAttribute("allEtablissements", fetchEtablissements());
        model.addAttribute("allDepots", fetchAllDepots());
        model.addAttribute("assignedEtabIds", new java.util.ArrayList<Long>());
        model.addAttribute("assignedDepotIds", new java.util.ArrayList<Long>());
        return "user-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("profiles", profileService.findAll());
        model.addAttribute("allEtablissements", fetchEtablissements());
        model.addAttribute("allDepots", fetchAllDepots());

        Map<String, Object> assignments = fetchUserAssignments(id);
        model.addAttribute("assignedEtabIds", assignments.getOrDefault("etablissementIds", new java.util.ArrayList<>()));
        model.addAttribute("assignedDepotIds", assignments.getOrDefault("depotIds", new java.util.ArrayList<>()));
        model.addAttribute("userEtablissements", assignments.get("etablissements"));
        model.addAttribute("userDepots", assignments.get("depots"));

        return "user-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute User user,
                       @RequestParam(value = "etablissementIds", required = false) java.util.List<Long> etablissementIds,
                       @RequestParam(value = "depotIds", required = false) java.util.List<Long> depotIds) {
        User savedUser = userService.save(user);
        saveAssignments(savedUser.getId(), etablissementIds, depotIds);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/toggle-active/{id}")
    public String toggleActive(@PathVariable Long id) {
        userService.toggleActive(id);
        return "redirect:/users";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allEtablissements", fetchEtablissements());

        Map<String, Object> assignments = fetchUserAssignments(id);
        model.addAttribute("assignedEtabIds", assignments.get("etablissementIds"));
        model.addAttribute("assignedDepotIds", assignments.get("depotIds"));

        return "user-details";
    }

    private java.util.List<java.util.Map<String, Object>> fetchEtablissements() {
        try {
            java.util.List<java.util.Map<String, Object>> result = restTemplate.getForObject(ETAB_SERVICE_URL + "/all", java.util.List.class);
            System.out.println("Fetched etablissements: " + (result != null ? result.size() : 0) + " items");
            return result != null ? result : java.util.Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching etablissements: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    private java.util.Map<String, Object> fetchUserAssignments(Long userId) {
        try {
            return restTemplate.getForObject(ETAB_SERVICE_URL + "/user/" + userId + "/assignments", java.util.Map.class);
        } catch (Exception e) {
            java.util.Map<String, Object> empty = new java.util.HashMap<>();
            empty.put("etablissementIds", new java.util.ArrayList<>());
            empty.put("depotIds", new java.util.ArrayList<>());
            return empty;
        }
    }

    private void saveAssignments(Long userId, java.util.List<Long> etablissementIds, java.util.List<Long> depotIds) {
        try {
            java.util.Map<String, Object> body = new java.util.HashMap<>();
            body.put("etablissementIds", etablissementIds != null ? etablissementIds : new java.util.ArrayList<>());
            body.put("depotIds", depotIds != null ? depotIds : new java.util.ArrayList<>());
            restTemplate.postForEntity(ETAB_SERVICE_URL + "/user/" + userId + "/assignments", body, Void.class);
        } catch (Exception e) {
            // Log error
            System.err.println("Failed to save assignments for user " + userId + ": " + e.getMessage());
        }
    }

    private java.util.List<java.util.Map<String, Object>> fetchAllDepots() {
        try {
            return restTemplate.getForObject(ETAB_SERVICE_URL + "/depots/all", java.util.List.class);
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }
}
