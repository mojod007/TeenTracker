package com.trace.controller;

import com.trace.entity.Gamme;
import com.trace.service.GammeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gammes")
@RequiredArgsConstructor
public class GammeRestController {

    private final GammeService gammeService;

    @PreAuthorize("hasAuthority('GAMME_VIEW')")
    @GetMapping
    public List<Gamme> getAllGammes() {
        return gammeService.getAllGammes();
    }

    @PreAuthorize("hasAuthority('GAMME_VIEW')")
    @GetMapping("/{id}")
    public Gamme getGammeById(@PathVariable Long id) {
        return gammeService.getGammeById(id).orElseThrow(() -> new RuntimeException("Gamme not found"));
    }

    @PreAuthorize("hasAuthority('GAMME_CREATE')")
    @PostMapping
    public Gamme createGamme(@RequestBody Gamme gamme) {
        return gammeService.saveGamme(gamme);
    }

    @PreAuthorize("hasAuthority('GAMME_UPDATE')")
    @PutMapping("/{id}")
    public Gamme updateGamme(@PathVariable Long id, @RequestBody Gamme gamme) {
        gamme.setId(id);
        return gammeService.saveGamme(gamme);
    }

    @PreAuthorize("hasAuthority('GAMME_DELETE')")
    @DeleteMapping("/{id}")
    public void deleteGamme(@PathVariable Long id) {
        gammeService.deleteGamme(id);
    }
}
