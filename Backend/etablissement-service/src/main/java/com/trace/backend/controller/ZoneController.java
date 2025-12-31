package com.trace.backend.controller;

import com.trace.backend.entity.Depot;
import com.trace.backend.entity.Zone;
import com.trace.backend.repository.DepotRepository;
import com.trace.backend.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/depots/{depotId}/zones")
public class ZoneController {

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private DepotRepository depotRepository;

    @GetMapping("/new")
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
    public String saveZone(@PathVariable Long depotId, @ModelAttribute Zone zone) {
        Optional<Depot> depot = depotRepository.findById(depotId);
        if (depot.isPresent()) {
            zone.setDepot(depot.get());
            zoneRepository.save(zone);
        }
        return "redirect:/depots/" + depotId + "/zones";
    }

    @GetMapping
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
