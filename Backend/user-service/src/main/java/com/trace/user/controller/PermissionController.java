package com.trace.user.controller;

import com.trace.user.entity.Permission;
import com.trace.user.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "permission-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("permission", new Permission());
        return "permission-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("permission", permissionService.findById(id));
        return "permission-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Permission permission) {
        permissionService.save(permission);
        return "redirect:/permissions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        permissionService.deleteById(id);
        return "redirect:/permissions";
    }
}
