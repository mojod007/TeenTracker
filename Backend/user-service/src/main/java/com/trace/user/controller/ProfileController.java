package com.trace.user.controller;

import com.trace.user.entity.Profile;
import com.trace.user.service.PermissionService;
import com.trace.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.trace.user.entity.Permission;

@Controller
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    public String list(Model model) {
        model.addAttribute("profiles", profileService.findAllWithUsers());
        return "profile-list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('PROFILE_CREATE')")
    public String addForm(Model model) {
        model.addAttribute("profile", new Profile());
        List<Permission> allPermissions = permissionService.findAll();
        model.addAttribute("permissionsByModule", allPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getModule() != null ? p.getModule() : "AUTRE")));
        return "profile-form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('PROFILE_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("profile", profileService.findById(id));
        List<Permission> allPermissions = permissionService.findAll();
        model.addAttribute("permissionsByModule", allPermissions.stream()
                .collect(Collectors.groupingBy(p -> p.getModule() != null ? p.getModule() : "AUTRE")));
        return "profile-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('PROFILE_CREATE') or hasAuthority('PROFILE_UPDATE')")
    public String save(@ModelAttribute Profile profile, @RequestParam(required = false) List<Long> permissionIds) {
        Profile savedProfile = profileService.save(profile);
        profileService.updatePermissions(savedProfile.getId(), permissionIds);
        return "redirect:/profiles";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PROFILE_DELETE')")
    public String delete(@PathVariable Long id) {
        profileService.deleteById(id);
        return "redirect:/profiles";
    }

    // REST endpoints for retrieving profiles
    @GetMapping("/api")
    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAllProfiles() {
        List<Profile> profiles = profileService.findAll();
        List<Map<String, Object>> profileMaps = profiles.stream().map(profile -> {
            Map<String, Object> profileMap = new java.util.HashMap<>();
            profileMap.put("id", profile.getId());
            profileMap.put("code", profile.getCode());
            profileMap.put("nom", profile.getNom());
            profileMap.put("description", profile.getDescription());
            profileMap.put("permissions", profile.getPermissions().stream()
                    .map(perm -> Map.of("id", perm.getId(), "code", perm.getCode(), "nom", perm.getNom()))
                    .collect(Collectors.toList()));
            return profileMap;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(profileMaps);
    }

    @GetMapping("/api/{id}")
    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProfileById(@PathVariable Long id) {
        Profile profile = profileService.findById(id);
        Map<String, Object> profileMap = new java.util.HashMap<>();
        profileMap.put("id", profile.getId());
        profileMap.put("code", profile.getCode());
        profileMap.put("nom", profile.getNom());
        profileMap.put("description", profile.getDescription());
        profileMap.put("permissions", profile.getPermissions().stream()
                .map(perm -> Map.of("id", perm.getId(), "code", perm.getCode(), "nom", perm.getNom()))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(profileMap);
    }
}
