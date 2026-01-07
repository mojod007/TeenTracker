package com.trace.user.controller;

import com.trace.user.entity.Permission;
import com.trace.user.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Controller
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    public String list(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "permission-list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('PROFILE_CREATE')")
    public String addForm(Model model) {
        model.addAttribute("permission", new Permission());
        return "permission-form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('PROFILE_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("permission", permissionService.findById(id));
        return "permission-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('PROFILE_CREATE') or hasAuthority('PROFILE_UPDATE')")
    public String save(@ModelAttribute Permission permission) {
        permissionService.save(permission);
        return "redirect:/permissions";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PROFILE_DELETE')")
    public String delete(@PathVariable Long id) {
        permissionService.deleteById(id);
        return "redirect:/permissions";
    }

    // REST endpoints for retrieving permissions
    @GetMapping("/api")
    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @ResponseBody
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/api/{id}")
    @PreAuthorize("hasAuthority('PROFILE_VIEW')")
    @ResponseBody
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Permission permission = permissionService.findById(id);
        return ResponseEntity.ok(permission);
    }
}
