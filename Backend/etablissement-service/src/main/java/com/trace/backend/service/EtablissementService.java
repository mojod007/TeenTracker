package com.trace.backend.service;

import com.trace.backend.client.UserServiceClient;
import com.trace.backend.entity.Depot;
import com.trace.backend.entity.Etablissement;
import com.trace.backend.entity.UserAssignment;
import com.trace.backend.repository.DepotRepository;
import com.trace.backend.repository.EtablissementRepository;
import com.trace.backend.repository.UserAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EtablissementService {

    private final EtablissementRepository etablissementRepository;
    private final DepotRepository depotRepository;
    private final UserAssignmentRepository userAssignmentRepository;
    private final UserServiceClient userServiceClient;

    public List<Etablissement> findAll() {
        log.info("Récupération de tous les établissements");
        return etablissementRepository.findAll();
    }

    public Page<Etablissement> findAll(Pageable pageable) {
        log.info("Récupération des établissements avec pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return etablissementRepository.findAll(pageable);
    }

    public Etablissement findById(Long id) {
        log.info("Recherche de l'établissement avec ID: {}", id);
        return etablissementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Établissement non trouvé avec l'ID: " + id));
    }

    public Etablissement save(Etablissement etablissement) {
        log.info("Sauvegarde de l'établissement: {}", etablissement.getNom());
        return etablissementRepository.save(etablissement);
    }

    public void deleteById(Long id) {
        log.info("Suppression de l'établissement avec ID: {}", id);
        if (!etablissementRepository.existsById(id)) {
            throw new RuntimeException("Établissement non trouvé avec l'ID: " + id);
        }
        etablissementRepository.deleteById(id);
    }

    public Etablissement createEtablissementWithDepot(String etabCode, String etabNom, boolean etabActif,
            String etabLocation,
            String depotCode, String depotNom, boolean depotActif, String depotLocation) {
        log.info("Création d'un établissement avec dépôt: {}", etabNom);

        // Create Etablissement
        Etablissement etablissement = new Etablissement();
        etablissement.setCode(etabCode);
        etablissement.setNom(etabNom);
        etablissement.setActif(etabActif);
        etablissement.setLocation(etabLocation);
        etablissement = etablissementRepository.save(etablissement);

        // Create Depot
        Depot depot = new Depot();
        depot.setCode(depotCode);
        depot.setNom(depotNom);
        depot.setActif(depotActif);
        depot.setLocation(depotLocation);
        depot.setEtablissement(etablissement);
        depotRepository.save(depot);

        return etablissement;
    }

    public void saveUserAssignments(Long userId, List<Long> etablissementIds, List<Long> depotIds) {
        log.info("Sauvegarde des assignations pour l'utilisateur: {}", userId);

        // Delete existing assignments
        userAssignmentRepository.deleteByUserId(userId);

        // Save new assignments
        if (etablissementIds != null) {
            for (Long etablissementId : etablissementIds) {
                UserAssignment assignment = new UserAssignment();
                assignment.setUserId(userId);
                assignment.setEtablissement(findById(etablissementId));
                userAssignmentRepository.save(assignment);
            }
        }

        if (depotIds != null) {
            for (Long depotId : depotIds) {
                UserAssignment assignment = new UserAssignment();
                assignment.setUserId(userId);
                Depot depot = depotRepository.findById(depotId)
                    .orElseThrow(() -> new RuntimeException("Dépôt non trouvé avec l'ID: " + depotId));
                assignment.setDepot(depot);
                userAssignmentRepository.save(assignment);
            }
        }
    }

    public Map<String, Object> getUserAssignments(Long userId) {
        log.info("Récupération des assignations pour l'utilisateur: {}", userId);

        Map<String, Object> assignments = new HashMap<>();
        assignments.put("etablissementIds", userAssignmentRepository.findEtablissementIdsByUserId(userId));
        assignments.put("depotIds", userAssignmentRepository.findDepotIdsByUserId(userId));

        // Fetch detailed etablissements and depots
        List<Map<String, Object>> etablissements = new java.util.ArrayList<>();
        List<Long> etabIds = userAssignmentRepository.findEtablissementIdsByUserId(userId);
        for (Long etabId : etabIds) {
            Etablissement etab = findById(etabId);
            Map<String, Object> etabMap = new HashMap<>();
            etabMap.put("id", etab.getId());
            etabMap.put("code", etab.getCode());
            etabMap.put("nom", etab.getNom());
            etabMap.put("actif", etab.isActif());
            etabMap.put("location", etab.getLocation());

            // Add depots for this etablissement
            List<Map<String, Object>> depotMaps = new java.util.ArrayList<>();
            if (etab.getDepots() != null) {
                for (Depot depot : etab.getDepots()) {
                    Map<String, Object> depotMap = new HashMap<>();
                    depotMap.put("id", depot.getId());
                    depotMap.put("code", depot.getCode());
                    depotMap.put("nom", depot.getNom());
                    depotMap.put("actif", depot.isActif());
                    depotMap.put("location", depot.getLocation());
                    depotMaps.add(depotMap);
                }
            }
            etabMap.put("depots", depotMaps);
            etablissements.add(etabMap);
        }
        assignments.put("etablissements", etablissements);

        // Fetch detailed depots
        List<Map<String, Object>> depots = new java.util.ArrayList<>();
        List<Long> depotIds = userAssignmentRepository.findDepotIdsByUserId(userId);
        for (Long depotId : depotIds) {
            Depot depot = depotRepository.findById(depotId)
                .orElseThrow(() -> new RuntimeException("Dépôt non trouvé avec l'ID: " + depotId));
            Map<String, Object> depotMap = new HashMap<>();
            depotMap.put("id", depot.getId());
            depotMap.put("code", depot.getCode());
            depotMap.put("nom", depot.getNom());
            depotMap.put("actif", depot.isActif());
            depotMap.put("location", depot.getLocation());
            depotMap.put("etablissement", Map.of(
                "id", depot.getEtablissement().getId(),
                "nom", depot.getEtablissement().getNom()
            ));
            depots.add(depotMap);
        }
        assignments.put("depots", depots);

        // Fetch user details from user-service
        try {
            Map<String, Object> userDetails = userServiceClient.getUserById(userId);
            assignments.put("user", userDetails);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des détails utilisateur: {}", e.getMessage());
            assignments.put("user", null);
        }

        return assignments;
    }

    public List<Map<String, Object>> getAllEtablissementsWithDepots() {
        log.info("Récupération de tous les établissements avec leurs dépôts");

        List<Etablissement> etablissements = findAll();
        List<Map<String, Object>> result = new java.util.ArrayList<>();

        for (Etablissement etab : etablissements) {
            Map<String, Object> etabMap = new HashMap<>();
            etabMap.put("id", etab.getId());
            etabMap.put("code", etab.getCode());
            etabMap.put("nom", etab.getNom());
            etabMap.put("actif", etab.isActif());
            etabMap.put("location", etab.getLocation());

            // Add depots
            List<Map<String, Object>> depotMaps = new java.util.ArrayList<>();
            if (etab.getDepots() != null) {
                for (Depot depot : etab.getDepots()) {
                    Map<String, Object> depotMap = new HashMap<>();
                    depotMap.put("id", depot.getId());
                    depotMap.put("code", depot.getCode());
                    depotMap.put("nom", depot.getNom());
                    depotMap.put("actif", depot.isActif());
                    depotMap.put("location", depot.getLocation());
                    depotMaps.add(depotMap);
                }
            }
            etabMap.put("depots", depotMaps);
            result.add(etabMap);
        }

        return result;
    }
}
