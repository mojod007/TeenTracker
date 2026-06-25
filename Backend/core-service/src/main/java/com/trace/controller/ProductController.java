package com.trace.controller;

import com.trace.entity.Product;
import com.trace.entity.enums.TypeGestion;
import com.trace.entity.enums.TypePalette;
import com.trace.service.ProductService;
import com.trace.service.GammeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "/products", "/produits", "/produit" })
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final GammeService gammeService;

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("gammes", gammeService.getAllGammes());
        model.addAttribute("typePalettes", TypePalette.values());
        model.addAttribute("typeGestions", TypeGestion.values());
        return "product-form";
    }

    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("gammes", gammeService.getAllGammes());
        model.addAttribute("typePalettes", TypePalette.values());
        model.addAttribute("typeGestions", TypeGestion.values());
        return "product-form";
    }

    @PreAuthorize("hasAuthority('PRODUCT_CREATE') or hasAuthority('PRODUCT_UPDATE')")
    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
