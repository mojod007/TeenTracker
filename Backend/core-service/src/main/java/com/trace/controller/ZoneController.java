package com.trace.controller;

import com.trace.entity.Depot;
import com.trace.entity.Zone;
import com.trace.repository.DepotRepository;
import com.trace.repository.ZoneRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/depots/{depotId}/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneRepository zoneRepository;
    private final DepotRepository depotRepository;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ZONE_CREATE')")
    public String newZone(@PathVariable Long depotId, Model model) {
        Optional<Depot> depot = depotRepository.findById(depotId);
        if (depot.isEmpty() || depot.get().getEtablissement() == null) {
            return "redirect:/etablissements";
        }
        model.addAttribute("depotId", depotId);
        model.addAttribute("depot", depot.get());
        model.addAttribute("zone", new Zone());
        return "zone-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ZONE_CREATE')")
    public String saveZone(@PathVariable Long depotId, @Valid @ModelAttribute Zone zone, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Optional<Depot> depot = depotRepository.findById(depotId);
            model.addAttribute("depotId", depotId);
            model.addAttribute("depot", depot.orElse(null));
            return "zone-form";
        }
        Optional<Depot> depot = depotRepository.findById(depotId);
        if (depot.isPresent()) {
            zone.setDepot(depot.get());
            zoneRepository.save(zone);
        }
        return "redirect:/depots/" + depotId + "/zones";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ZONE_VIEW')")
    public String listZones(@PathVariable Long depotId, Model model) {
        Optional<Depot> depot = depotRepository.findById(depotId);
        if (depot.isEmpty()) {
            return "redirect:/etablissements";
        }
        List<Zone> zones = zoneRepository.findByDepotId(depotId);
        model.addAttribute("depotId", depotId);
        model.addAttribute("zones", zones);
        model.addAttribute("depot", depot.get());
        return "zone-list";
    }
}
