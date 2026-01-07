package com.trace.backend.controller;

import com.trace.backend.entity.Depot;
import com.trace.backend.entity.Etablissement;
import com.trace.backend.repository.DepotRepository;
import com.trace.backend.repository.EtablissementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/etablissement")
public class DepotController {

    @Autowired
    private DepotRepository depotRepository;

    @Autowired
    private EtablissementRepository etablissementRepository;

    @GetMapping("")
    public String redirectToEtablissements() {
        return "redirect:/etablissements";
    }

    @GetMapping("/{etabId}/depots/new")
    @PreAuthorize("hasAuthority('DEPOT_CREATE')")
    public String newDepot(@PathVariable Long etabId, Model model) {
        Optional<Etablissement> etablissement = etablissementRepository.findById(etabId);
        if (etablissement.isEmpty()) {
            return "redirect:/etablissements";
        }
        model.addAttribute("etabId", etabId);
        model.addAttribute("depot", new Depot());
        return "depot-form";
    }

    @PostMapping("/{etabId}/depots/save")
    @PreAuthorize("hasAuthority('DEPOT_CREATE')")
    public String saveDepot(@PathVariable Long etabId, @ModelAttribute Depot depot) {
        Optional<Etablissement> etablissement = etablissementRepository.findById(etabId);
        if (etablissement.isPresent()) {
            depot.setEtablissement(etablissement.get());
            depotRepository.save(depot);
        }
        return "redirect:/etablissement/" + etabId + "/depots";
    }

    @GetMapping("/{etabId}/depots")
    @PreAuthorize("hasAuthority('DEPOT_VIEW')")
    public String listDepots(@PathVariable Long etabId, Model model) {
        Optional<Etablissement> etablissement = etablissementRepository.findById(etabId);
        if (etablissement.isEmpty()) {
            return "redirect:/etablissements";
        }
        List<Depot> depots = depotRepository.findByEtablissementId(etabId);
        model.addAttribute("etabId", etabId);
        model.addAttribute("depots", depots);
        model.addAttribute("etablissement", etablissement.get());
        return "depot-list";
    }
}
