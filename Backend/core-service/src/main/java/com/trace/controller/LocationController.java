package com.trace.controller;

import com.trace.entity.Location;
import com.trace.entity.Zone;
import com.trace.repository.LocationRepository;
import com.trace.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/zones/{zoneId}/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository locationRepository;
    private final ZoneRepository zoneRepository;

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('LOCATION_CREATE')")
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
    @PreAuthorize("hasAuthority('LOCATION_CREATE')")
    public String saveLocation(@PathVariable Long zoneId, @ModelAttribute Location location) {
        Optional<Zone> zone = zoneRepository.findById(zoneId);
        if (zone.isPresent()) {
            location.setZone(zone.get());
            locationRepository.save(location);
        }
        return "redirect:/zones/" + zoneId + "/locations";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('LOCATION_VIEW')")
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
    @PreAuthorize("hasAuthority('LOCATION_UPDATE')")
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
    @PreAuthorize("hasAuthority('LOCATION_UPDATE')")
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
    @PreAuthorize("hasAuthority('LOCATION_DELETE')")
    public String deleteLocation(@PathVariable Long zoneId, @PathVariable Long id) {
        locationRepository.deleteById(id);
        return "redirect:/zones/" + zoneId + "/locations";
    }
}
