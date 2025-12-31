package com.trace.backend.controller;

import com.trace.backend.entity.Location;
import com.trace.backend.entity.Zone;
import com.trace.backend.repository.LocationRepository;
import com.trace.backend.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/zones/{zoneId}/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @GetMapping("/new")
    public String newLocation(@PathVariable Long zoneId, Model model) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        if (zone.isEmpty() || zone.get().getDepot() == null) {
            return "redirect:/etablissements";
        }
        model.addAttribute("zoneId", zoneId);
        model.addAttribute("zone", zone.get());
        model.addAttribute("location", new Location());
        return "location-form";
    }

    @PostMapping("/save")
    public String saveLocation(@PathVariable Long zoneId, @ModelAttribute Location location) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        if (zone.isPresent()) {
            location.setZone(zone.get());
            locationRepository.save(location);
        }
        return "redirect:/zones/" + zoneId + "/locations";
    }

    @GetMapping
    public String listLocations(@PathVariable Long zoneId, Model model) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        if (zone.isEmpty()) {
            return "redirect:/etablissements";
        }
        List<Location> locations = locationRepository.findByZoneId(zoneId);
        model.addAttribute("zoneId", zoneId);
        model.addAttribute("locations", locations);
        model.addAttribute("zone", zone.get());
        return "location-list";
    }

    @GetMapping("/{id}/edit")
    public String editLocation(@PathVariable Long zoneId, @PathVariable Long id, Model model) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        Optional<Location> location = locationRepository.findById(id);
        if (zone.isEmpty() || location.isEmpty()) {
            return "redirect:/zones/" + zoneId + "/locations";
        }
        model.addAttribute("zoneId", zoneId);
        model.addAttribute("zone", zone.get());
        model.addAttribute("location", location.get());
        model.addAttribute("isEdit", true);
        return "location-form";
    }

    @PostMapping("/{id}/update")
    public String updateLocation(@PathVariable Long zoneId, @PathVariable Long id, @ModelAttribute Location location) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        if (zone.isPresent()) {
            location.setId(id);
            location.setZone(zone.get());
            locationRepository.save(location);
        }
        return "redirect:/zones/" + zoneId + "/locations";
    }

    @GetMapping("/{id}/delete")
    public String deleteLocation(@PathVariable Long zoneId, @PathVariable Long id) {
        locationRepository.deleteById(id);
        return "redirect:/zones/" + zoneId + "/locations";
    }
}
