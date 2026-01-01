package com.trace.backend.controller;

import com.trace.backend.entity.Etablissement;
import com.trace.backend.service.EtablissementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/etablissements")
@RequiredArgsConstructor
public class EtablissementRestController {

    private final EtablissementService etablissementService;

    @GetMapping
    public ResponseEntity<List<Etablissement>> getAllEtablissements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (page == 0 && size == 10) {
            // Return all if no pagination parameters
            return ResponseEntity.ok(etablissementService.findAll());
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Etablissement> etablissementPage = etablissementService.findAll(pageable);
            return ResponseEntity.ok(etablissementPage.getContent());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etablissement> getEtablissementById(@PathVariable Long id) {
        Etablissement etablissement = etablissementService.findById(id);
        return ResponseEntity.ok(etablissement);
    }

    @PostMapping
    public ResponseEntity<Etablissement> createEtablissement(@Valid @RequestBody Etablissement etablissement) {
        Etablissement savedEtablissement = etablissementService.save(etablissement);
        return ResponseEntity.ok(savedEtablissement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Etablissement> updateEtablissement(@PathVariable Long id, @Valid @RequestBody Etablissement etablissement) {
        etablissement.setId(id);
        Etablissement updatedEtablissement = etablissementService.save(etablissement);
        return ResponseEntity.ok(updatedEtablissement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtablissement(@PathVariable Long id) {
        etablissementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllEtablissementsWithDepots() {
        List<Map<String, Object>> result = etablissementService.getAllEtablissementsWithDepots();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}/assignments")
    public ResponseEntity<Map<String, Object>> getUserAssignments(@PathVariable Long userId) {
        Map<String, Object> assignments = etablissementService.getUserAssignments(userId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/user/{userId}/assignments")
    public ResponseEntity<Void> saveUserAssignments(@PathVariable Long userId,
                                                   @RequestBody Map<String, List<Long>> assignments) {
        List<Long> etablissementIds = assignments.get("etablissementIds");
        List<Long> depotIds = assignments.get("depotIds");
        etablissementService.saveUserAssignments(userId, etablissementIds, depotIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/depots/all")
    public ResponseEntity<List<Map<String, Object>>> getAllDepots() {
        List<Map<String, Object>> depots = etablissementService.getAllDepots();
        return ResponseEntity.ok(depots);
    }
}
