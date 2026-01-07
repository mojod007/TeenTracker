package com.trace.backend.controller;

import com.trace.backend.entity.Etablissement;
import com.trace.backend.service.EtablissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@Controller
@RequestMapping("/etablissements")
public class EtablissementController {

    @Autowired
    private EtablissementService etablissementService;

    @Autowired
    private com.trace.backend.repository.EtablissementRepository etablissementRepository;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ETABLISSEMENT_CREATE')")
    public String newEtablissement(Model model) {
        model.addAttribute("etablissement", new Etablissement());
        return "etablissement-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ETABLISSEMENT_CREATE')")
    public String saveEtablissement(@RequestParam String etabCode, @RequestParam String etabNom,
            @RequestParam(defaultValue = "true") boolean etabActif, @RequestParam String etabLocation,
            @RequestParam String depotCode, @RequestParam String depotNom,
            @RequestParam(defaultValue = "true") boolean depotActif, @RequestParam String depotLocation) {
        etablissementService.createEtablissementWithDepot(etabCode, etabNom, etabActif, etabLocation,
                depotCode, depotNom, depotActif, depotLocation);
        return "redirect:/etablissements";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ETABLISSEMENT_VIEW')")
    public String listEtablissements(Model model) {
        List<Etablissement> etablissements = etablissementRepository.findAll();
        model.addAttribute("etablissements", etablissements);
        return "etablissement-list";
    }
}
