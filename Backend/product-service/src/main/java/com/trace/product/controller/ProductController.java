package com.trace.product.controller;

import com.trace.product.entity.Product;
import com.trace.product.entity.TypePalette;
import com.trace.product.enums.TypeGestion;
import com.trace.product.service.ProductService;
import com.trace.product.service.GammeService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("gammes", gammeService.getAllGammes());
        model.addAttribute("typePalettes", TypePalette.values());
        model.addAttribute("typeGestions", TypeGestion.values());
        return "product-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("gammes", gammeService.getAllGammes());
        model.addAttribute("typePalettes", TypePalette.values());
        model.addAttribute("typeGestions", TypeGestion.values());
        return "product-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
