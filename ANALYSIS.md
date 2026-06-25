# Analyse du Projet TeenTracker (Trace)

> **Version** : 0.0.1-SNAPSHOT | **Statut** : En développement actif | **Repository** : https://github.com/mejdoumo/Trace

---

## 1. Vue d'ensemble

**Trace** est un système de **gestion de traçabilité logistique** en architecture microservices. Il permet de gérer des établissements, dépôts, zones de stockage, produits et utilisateurs avec un contrôle d'accès par rôles (RBAC).

### Objectifs métier
- Gestion hiérarchique des infrastructures (établissement → dépôt → zone → emplacement)
- Gestion du catalogue produits et gammes
- Contrôle d'accès granulaire par permissions
- Interface d'administration unifiée

---

## 2. Stack technique

### Backend

| Technologie | Version | Usage |
|---|---|---|
| Java | 17 | Langage principal |
| Spring Boot | 3.4.0 | Framework applicatif |
| Spring Cloud | 2024.0.0 | Infrastructure microservices |
| Spring Data JPA | (Spring Boot) | ORM / persistance |
| Spring Security | (Spring Boot) | Authentification & autorisation |
| Spring Cloud Gateway | (Spring Cloud) | API Gateway |
| Netflix Eureka | (Spring Cloud) | Service Discovery |
| OpenFeign | (Spring Cloud) | Communication inter-services |
| H2 Database | (embarqué) | Base de données développement |
| PostgreSQL | - | Base de données cible (production) |
| Lombok | - | Boilerplate reduction |
| jjwt (io.jsonwebtoken) | 0.11.5 | JWT |
| Thymeleaf | (Spring Boot) | Moteur de templates HTML |

### Frontend (rendu serveur)

| Technologie | Version | Usage |
|---|---|---|
| Thymeleaf | - | Templates serveur |
| AdminLTE 3 | 3.x | Dashboard UI |
| Bootstrap | 4.6 | Framework CSS |
| jQuery | 3.6 | JavaScript |
| Font Awesome | 5.15 | Icônes |

### Infrastructure & Outils

| Outil | Usage |
|---|---|
| Maven | Build & dépendances |
| Eureka Dashboard | Console de découverte (port 8761) |
| Spring Boot Actuator | Health checks |
| SLF4J/Logback | Logging structuré |
| IntelliJ IDEA / VS Code | Environnements de développement |

---

## 3. Architecture

### Diagramme des services

```
                            +-----------+
                            |  Browser  |
                            +-----+-----+
                                  |
                            (port 8080)
                                  |
                         +--------v---------+
                         |  Gateway Service  |
                         |    port 8080      |
                         +---+----+----+----+
                             |    |    |    |
              +--------------+    |    |    +--------------+
              |                   |    |                   |
     +--------v------+    +-------v----v-------+    +------v--------+
     | Discovery Svc |    |   REST APIs       |    | Dashboard Svc |
     | (Eureka)      |    |   (inter-srv)     |    | port 8083     |
     | port 8761     |    +--+---+---+---+----+    +---------------+
     +---------------+       |   |   |   |
                             |   |   |   |
              +--------------+   |   |   +--------------+
              |                  |   |                  |
     +--------v------+   +-------v---v-------+   +------v--------+
     | User Service  |   | Product Service   |   | Etablissement |
     | port 8084     |   | port 8082         |   | Service       |
     | H2: userdb    |   | H2: product_db    |   | port 8081     |
     +-------+-------+   +---------+---------+   | H2: etab_db   |
             |                     |             +-------+-------+
             |              +------v------+              |
             +--------------> Auth Service <-------------+
                            | port 8085    |
                            | H2: auth_db  |
                            +--------------+
```

### Microservices

| # | Service | Port | Rôle |
|---|---|---|---|
| 1 | **discovery-service** | 8761 | Annuaire Eureka (enregistrement/découverte) |
| 2 | **gateway-service** | 8080 | Point d'entrée unique, routage, load balancing |
| 3 | **dashboard-service** | 8083 | Page d'accueil, tableau de bord, navigation |
| 4 | **user-service** | 8084 | Utilisateurs, profils, permissions |
| 5 | **auth-service** | 8085 | Authentification JWT, login |
| 6 | **product-service** | 8082 | Produits et gammes de produits |
| 7 | **etablissement-service** | 8081 | Établissements, dépôts, zones, emplacements |

### Principes architecturaux

- **Single Entry Point** : Toutes les requêtes passent par le Gateway (port 8080)
- **Database per Service** : Chaque microservice possède sa propre base de données H2
- **Communication REST** : APIs RESTful pour les échanges inter-services (RestTemplate + OpenFeign)
- **JWT Stateless** : Authentification via JWT stocké en HttpOnly cookie
- **Service Discovery** : Eureka pour la localisation dynamique des services

### Ordre de démarrage (critique)

1. Discovery Service (attendre 60s)
2. Gateway Service (attendre 10s)
3. Dashboard Service (attendre 10s)
4. User Service (attendre 10s)
5. Product Service (attendre 10s)
6. Etablissement Service (attendre 30s)
7. Auth Service (attendre 10s)

---

## 4. Modèle de données

### Relations entre entités

```
Établissement 1───* Dépôt 1───* Zone 1───* Emplacement
Gamme 1───* Produit
Profil *───* Permission
Profil 1───* Utilisateur
UserAssignment relie User → Établissement/Dépôt
```

### Entités détaillées

#### Établissement Service
- **Etablissement** : `id`, `code`, `nom`, `actif`, `location`
- **Depot** : `id`, `code`, `nom`, `actif`, `location`, `etablissement` (FK)
- **Zone** : `id`, `code`, `nom`, `actif`, `description`, `depot` (FK)
- **Emplacement** : `id`, `code`, `nom`, `hauteur`, `largeur`, `profondeur`, `poidsMax`, `typeGestion` (FIFO/LIFO/FEFO/MANUEL)

#### Product Service
- **Product** : `id`, `code`, `nom`, `description`, `prix`, `poids`, `peremption`, `minqu`, `maxqu`, `typePalette`, `typeGestion`, `gamme` (FK)
- **Gamme** : `id`, `code`, `nom`, `description`, `actif`

#### User Service
- **User** : `id`, `username`, `email`, `password` (BCrypt), `firstName`, `lastName`, `active`, `profile` (FK)
- **Profile** : `id`, `name`, `description`, `active`, `permissions` (M2M)
- **Permission** : `id`, `name`, `description`, `active` (35+ permissions granulaires)
- **UserAssignment** : Lie un utilisateur à un établissement/dépôt

---

## 5. Fonctionnalités

### Implémentées ✅

| Module | Fonctionnalités |
|---|---|
| **Établissements** | CRUD, pagination, activation/désactivation |
| **Dépôts** | CRUD, pagination, association établissement |
| **Zones** | CRUD, pagination, association dépôt |
| **Emplacements** | CRUD, dimensions physiques, type de gestion (FIFO/LIFO/FEFO/MANUEL) |
| **Produits** | CRUD, pagination, gamme, caractéristiques avancées |
| **Gammes** | CRUD, classification produits |
| **Utilisateurs** | CRUD, profils, permissions (RBAC), association établissement/dépôt |
| **Authentification** | Login JWT, HttpOnly cookie, filtre inter-services |
| **Infrastructure** | Eureka, Gateway, Dashboard centralisé, scripts start/stop |

### En développement 🚧

- Application mobile (Angular/Ionic) — dossier `Mobile/` vide
- Gestion des stocks
- Traçabilité des mouvements
- Rapports et statistiques
- Export PDF/Excel
- Notifications temps réel

---

## 6. Sécurité

### Mécanismes en place
- **JWT** : Authentification stateless, stocké en HttpOnly cookie
- **BCrypt** : Hashing des mots de passe
- **@PreAuthorize** : Contrôle d'accès par annotations sur toutes les méthodes de controllers
- **Permissions granulaires** : 35+ permissions (USER, PROFILE, PRODUCT, GAMME, ETABLISSEMENT, DEPOT, ZONE, LOCATION, DASHBOARD)
- **Profils** : Admin, Manager, User (pré-seedés)

### Améliorations appliquées (cf. SECURITY_IMPROVEMENTS_WALKTHROUGH.md)
- Secrets externalisés via variables d'environnement (`JWT_SECRET`, `DB_PASSWORD`)
- Validation `@Validated` sur toutes les entités JPA
- Global exception handlers (JSON pour API, HTML pour navigateur)
- Profils Spring `dev`/`prod` (H2 vs PostgreSQL, debug vs warn)
- Console H2 désactivée par défaut

---

## 7. Tests

| Service | Tests |
|---|---|
| **user-service** | `UserServiceTest`, `ProfileServiceTest` |
| **product-service** | `ProductServiceTest`, `ProductRestControllerIntegrationTest` |

Tests partiels — couverture à étendre.

---

## 8. Structure du projet

```
TeenTracker/
├── Backend/
│   ├── pom.xml                          # POM parent (multi-module)
│   ├── discovery-service/               # Eureka Server
│   ├── gateway-service/                 # Spring Cloud Gateway
│   ├── dashboard-service/               # Page d'accueil
│   ├── user-service/                    # Utilisateurs & RBAC
│   ├── auth-service/                    # Authentification JWT
│   ├── product-service/                 # Produits & gammes
│   ├── etablissement-service/           # Établissements, dépôts, zones
│   ├── data/                            # Bases H2 persistées
│   └── logs/                            # Logs par service
├── Mobile/                              # (vide) Future app mobile
├── start_all.bat                        # Démarrage séquentiel
├── stop_all.bat                         # Arrêt des services
├── README.md                            # Documentation
├── SECURITY_IMPROVEMENTS_WALKTHROUGH.md # Audit sécurité
├── TODO.md                              # Tâches effectuées
└── ANALYSE.md                           # Ce fichier
```

### Structure type d'un microservice

```
service-name/
├── src/main/java/com/trace/[domain]/
│   ├── controller/       # Contrôleurs REST et Web
│   ├── service/          # Logique métier
│   ├── repository/       # Spring Data repositories
│   ├── entity/           # Entités JPA
│   ├── config/           # Configuration (Security, Data, etc.)
│   └── *Application.java # Classe main
├── src/main/resources/
│   ├── templates/        # Thymeleaf
│   ├── static/           # CSS/JS/Assets
│   └── application.yml   # Configuration
└── pom.xml
```

---

## 9. Points d'attention

| Problème | Détail | Recommandation |
|---|---|---|
| `.gitignore` vide | Aucune règle d'exclusion | Ajouter `.gitignore` (cibles, JAR, logs, H2, `.idea/`, `.vscode/`) |
| Base H2 fichier unique | Données non partagées, perte possible | Migrer vers PostgreSQL (déjà prévu en prod) |
| Démarrage séquentiel | Dépendances temporelles fragiles | Docker Compose ou Kubernetes pour l'orchestration |
| Pas de CI/CD | Aucun pipeline automatisé | Mettre en place GitHub Actions |
| Tests partiels | Seulement 2 services testés | Étendre la couverture à tous les services |
| Pas de contrat API | Aucune spécification OpenAPI | Ajouter springdoc-openapi |
| Frontend monolithique | Thymeleaf = rendu serveur, pas de SPA | Prévu pour la phase mobile (Angular/Ionic) |

---

## 10. Roadmap

| Phase | Statut | Contenu |
|---|---|---|
| **Phase 1 : Backend Core** | ✅ Complétée | Microservices, Eureka, Gateway, CRUD, AdminLTE |
| **Phase 2 : Sécurité** | 🔄 En cours | JWT, RBAC, permissions (largement avancée) |
| **Phase 3 : Fonctionnalités avancées** | ⏳ À venir | Stocks, traçabilité, alertes, rapports |
| **Phase 4 : Application mobile** | ⏳ À venir | Angular/Ionic, offline, barcode, géolocalisation |
| **Phase 5 : Production** | ⏳ À venir | PostgreSQL, Docker, CI/CD, monitoring, Swagger |

---

## 11. Utilisateurs pré-seedés (dev)

| Utilisateur | Mot de passe | Profil |
|---|---|---|
| admin | admin123 | Admin |
| manager | manager123 | Manager |
| user | user123 | User |

---

**Dernière analyse** : Juin 2026
