package com.trace.product.controller;

import com.trace.product.entity.Gamme;
import com.trace.product.service.GammeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gammes")
@RequiredArgsConstructor
public class GammeRestController {
    private final GammeService gammeService;

    @GetMapping
    public List<Gamme> getAllGammes() {
        return gammeService.getAllGammes();
    }

    @GetMapping("/{id}")
    public Gamme getGammeById(@PathVariable Long id) {
        return gammeService.getGammeById(id).orElseThrow(() -> new RuntimeException("Gamme not found"));
    }

    @PostMapping
    public Gamme createGamme(@RequestBody Gamme gamme) {
        return gammeService.saveGamme(gamme);
    }

    @PutMapping("/{id}")
    public Gamme updateGamme(@PathVariable Long id, @RequestBody Gamme gamme) {
        gamme.setId(id);
        return gammeService.saveGamme(gamme);
    }

    @DeleteMapping("/{id}")
    public void deleteGamme(@PathVariable Long id) {
        gammeService.deleteGamme(id);
    }
}
