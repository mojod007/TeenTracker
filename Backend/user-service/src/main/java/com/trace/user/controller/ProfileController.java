package com.trace.user.controller;

import com.trace.user.entity.Profile;
import com.trace.user.service.PermissionService;
import com.trace.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final PermissionService permissionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("profiles", profileService.findAll());
        return "profile-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("permissions", permissionService.findAll());
        return "profile-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.findById(id));
        model.addAttribute("permissions", permissionService.findAll());
        return "profile-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Profile profile, @RequestParam(required = false) List<Long> permissionIds) {
        Profile savedProfile = profileService.save(profile);
        
        // Clear existing permissions and add selected ones
        savedProfile.getPermissions().clear();
        if (permissionIds != null) {
            for (Long permId : permissionIds) {
                profileService.addPermission(savedProfile.getId(), permId);
            }
        }
        
        return "redirect:/profiles";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        profileService.deleteById(id);
        return "redirect:/profiles";
    }
}
