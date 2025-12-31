package com.trace.user.config;

import com.trace.user.entity.Permission;
import com.trace.user.entity.Profile;
import com.trace.user.entity.User;
import com.trace.user.repository.PermissionRepository;
import com.trace.user.repository.ProfileRepository;
import com.trace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Vérification et initialisation des données...");

        // Vérifier si les données existent déjà
        if (permissionRepository.count() > 0) {
            log.info("Les données existent déjà, skipping initialization");
            return;
        }

        log.info("Aucune donnée trouvée, initialisation en cours...");

        // Créer les permissions
        createPermissions();

        // Créer les profils
        createProfiles();

        // Créer les utilisateurs par défaut
        createDefaultUsers();

        log.info("Initialisation des données terminée avec succès");
    }

    private void createPermissions() {
        List<Permission> permissions = Arrays.asList(
            // User Module
            Permission.builder().code("USER_VIEW").nom("Voir les utilisateurs").description("Permet de consulter la liste des utilisateurs").module("USER").build(),
            Permission.builder().code("USER_CREATE").nom("Créer un utilisateur").description("Permet de créer de nouveaux utilisateurs").module("USER").build(),
            Permission.builder().code("USER_UPDATE").nom("Modifier un utilisateur").description("Permet de modifier les informations des utilisateurs").module("USER").build(),
            Permission.builder().code("USER_DELETE").nom("Supprimer un utilisateur").description("Permet de supprimer des utilisateurs").module("USER").build(),
            Permission.builder().code("USER_ASSIGN").nom("Assigner établissements/dépôts").description("Permet d'assigner des établissements et dépôts aux utilisateurs").module("USER").build(),

            // Profile Module
            Permission.builder().code("PROFILE_VIEW").nom("Voir les profils").description("Permet de consulter la liste des profils").module("PROFILE").build(),
            Permission.builder().code("PROFILE_CREATE").nom("Créer un profil").description("Permet de créer de nouveaux profils").module("PROFILE").build(),
            Permission.builder().code("PROFILE_UPDATE").nom("Modifier un profil").description("Permet de modifier les profils").module("PROFILE").build(),
            Permission.builder().code("PROFILE_DELETE").nom("Supprimer un profil").description("Permet de supprimer des profils").module("PROFILE").build(),

            // Product Module
            Permission.builder().code("PRODUCT_VIEW").nom("Voir les produits").description("Permet de consulter le catalogue de produits").module("PRODUCT").build(),
            Permission.builder().code("PRODUCT_CREATE").nom("Créer un produit").description("Permet de créer de nouveaux produits").module("PRODUCT").build(),
            Permission.builder().code("PRODUCT_UPDATE").nom("Modifier un produit").description("Permet de modifier les produits").module("PRODUCT").build(),
            Permission.builder().code("PRODUCT_DELETE").nom("Supprimer un produit").description("Permet de supprimer des produits").module("PRODUCT").build(),

            // Gamme Module
            Permission.builder().code("GAMME_VIEW").nom("Voir les gammes").description("Permet de consulter les gammes de produits").module("GAMME").build(),
            Permission.builder().code("GAMME_CREATE").nom("Créer une gamme").description("Permet de créer de nouvelles gammes").module("GAMME").build(),
            Permission.builder().code("GAMME_UPDATE").nom("Modifier une gamme").description("Permet de modifier les gammes").module("GAMME").build(),
            Permission.builder().code("GAMME_DELETE").nom("Supprimer une gamme").description("Permet de supprimer des gammes").module("GAMME").build(),

            // Etablissement Module
            Permission.builder().code("ETABLISSEMENT_VIEW").nom("Voir les établissements").description("Permet de consulter les établissements").module("ETABLISSEMENT").build(),
            Permission.builder().code("ETABLISSEMENT_CREATE").nom("Créer un établissement").description("Permet de créer de nouveaux établissements").module("ETABLISSEMENT").build(),
            Permission.builder().code("ETABLISSEMENT_UPDATE").nom("Modifier un établissement").description("Permet de modifier les établissements").module("ETABLISSEMENT").build(),
            Permission.builder().code("ETABLISSEMENT_DELETE").nom("Supprimer un établissement").description("Permet de supprimer des établissements").module("ETABLISSEMENT").build(),

            // Depot Module
            Permission.builder().code("DEPOT_VIEW").nom("Voir les dépôts").description("Permet de consulter les dépôts").module("DEPOT").build(),
            Permission.builder().code("DEPOT_CREATE").nom("Créer un dépôt").description("Permet de créer de nouveaux dépôts").module("DEPOT").build(),
            Permission.builder().code("DEPOT_UPDATE").nom("Modifier un dépôt").description("Permet de modifier les dépôts").module("DEPOT").build(),
            Permission.builder().code("DEPOT_DELETE").nom("Supprimer un dépôt").description("Permet de supprimer des dépôts").module("DEPOT").build(),

            // Zone Module
            Permission.builder().code("ZONE_VIEW").nom("Voir les zones").description("Permet de consulter les zones").module("ZONE").build(),
            Permission.builder().code("ZONE_CREATE").nom("Créer une zone").description("Permet de créer de nouvelles zones").module("ZONE").build(),
            Permission.builder().code("ZONE_UPDATE").nom("Modifier une zone").description("Permet de modifier les zones").module("ZONE").build(),
            Permission.builder().code("ZONE_DELETE").nom("Supprimer une zone").description("Permet de supprimer des zones").module("ZONE").build(),

            // Location Module
            Permission.builder().code("LOCATION_VIEW").nom("Voir les emplacements").description("Permet de consulter les emplacements").module("LOCATION").build(),
            Permission.builder().code("LOCATION_CREATE").nom("Créer un emplacement").description("Permet de créer de nouveaux emplacements").module("LOCATION").build(),
            Permission.builder().code("LOCATION_UPDATE").nom("Modifier un emplacement").description("Permet de modifier les emplacements").module("LOCATION").build(),
            Permission.builder().code("LOCATION_DELETE").nom("Supprimer un emplacement").description("Permet de supprimer des emplacements").module("LOCATION").build(),

            // Dashboard Module
            Permission.builder().code("DASHBOARD_VIEW").nom("Voir le tableau de bord").description("Permet d'accéder au tableau de bord").module("DASHBOARD").build(),
            Permission.builder().code("DASHBOARD_ADMIN").nom("Administration du tableau de bord").description("Permet d'administrer le tableau de bord").module("DASHBOARD").build()
        );

        permissionRepository.saveAll(permissions);
        log.info("Permissions créées: {}", permissions.size());
    }

    private void createProfiles() {
        // Créer les profils
        Profile adminProfile = Profile.builder()
            .code("ADMIN")
            .nom("Administrateur")
            .description("Accès complet à toutes les fonctionnalités du système")
            .build();

        Profile managerProfile = Profile.builder()
            .code("MANAGER")
            .nom("Gestionnaire")
            .description("Gestion des produits et établissements, consultation des utilisateurs")
            .build();

        Profile userProfile = Profile.builder()
            .code("USER")
            .nom("Utilisateur")
            .description("Consultation uniquement des produits et établissements")
            .build();

        profileRepository.saveAll(Arrays.asList(adminProfile, managerProfile, userProfile));

        // Assigner les permissions aux profils
        assignPermissionsToProfiles();

        log.info("Profils créés: 3");
    }

    private void assignPermissionsToProfiles() {
        // ADMIN - All permissions
        Profile admin = profileRepository.findByCode("ADMIN").orElseThrow();
        admin.setPermissions(permissionRepository.findAll());
        profileRepository.save(admin);

        // MANAGER - Management permissions
        Profile manager = profileRepository.findByCode("MANAGER").orElseThrow();
        List<Long> managerPermissionIds = Arrays.asList(1L, 5L, 6L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L,
                                                        18L, 19L, 20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L,
                                                        30L, 31L, 32L, 33L, 34L);
        manager.setPermissions(permissionRepository.findAllById(managerPermissionIds));
        profileRepository.save(manager);

        // USER - Read-only permissions
        Profile user = profileRepository.findByCode("USER").orElseThrow();
        List<Long> userPermissionIds = Arrays.asList(10L, 14L, 18L, 22L, 26L, 30L, 34L);
        user.setPermissions(permissionRepository.findAllById(userPermissionIds));
        profileRepository.save(user);
    }

    private void createDefaultUsers() {
        LocalDateTime now = LocalDateTime.now();

        // Admin user
        User admin = User.builder()
            .username("admin")
            .email("admin@trace.com")
            .password(passwordEncoder.encode("admin123"))
            .firstName("Admin")
            .lastName("System")
            .active(true)
            .profile(profileRepository.findByCode("ADMIN").orElseThrow())
            .createdAt(now)
            .updatedAt(now)
            .build();

        // Manager user
        User manager = User.builder()
            .username("manager")
            .email("manager@trace.com")
            .password(passwordEncoder.encode("manager123"))
            .firstName("Manager")
            .lastName("User")
            .active(true)
            .profile(profileRepository.findByCode("MANAGER").orElseThrow())
            .createdAt(now)
            .updatedAt(now)
            .build();

        // Standard user
        User standardUser = User.builder()
            .username("user")
            .email("user@trace.com")
            .password(passwordEncoder.encode("user123"))
            .firstName("Standard")
            .lastName("User")
            .active(true)
            .profile(profileRepository.findByCode("USER").orElseThrow())
            .createdAt(now)
            .updatedAt(now)
            .build();

        userRepository.saveAll(Arrays.asList(admin, manager, standardUser));
        log.info("Utilisateurs par défaut créés: 3");
    }
}
