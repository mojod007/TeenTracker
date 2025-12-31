-- Permissions
INSERT INTO permissions (id, code, nom, description, module) VALUES
-- User Module
(1, 'USER_VIEW', 'Voir les utilisateurs', 'Permet de consulter la liste des utilisateurs', 'USER'),
(2, 'USER_CREATE', 'Créer un utilisateur', 'Permet de créer de nouveaux utilisateurs', 'USER'),
(3, 'USER_UPDATE', 'Modifier un utilisateur', 'Permet de modifier les informations des utilisateurs', 'USER'),
(4, 'USER_DELETE', 'Supprimer un utilisateur', 'Permet de supprimer des utilisateurs', 'USER'),
(5, 'USER_ASSIGN', 'Assigner établissements/dépôts', 'Permet d\'assigner des établissements et dépôts aux utilisateurs', 'USER'),

-- Profile Module
(6, 'PROFILE_VIEW', 'Voir les profils', 'Permet de consulter la liste des profils', 'PROFILE'),
(7, 'PROFILE_CREATE', 'Créer un profil', 'Permet de créer de nouveaux profils', 'PROFILE'),
(8, 'PROFILE_UPDATE', 'Modifier un profil', 'Permet de modifier les profils', 'PROFILE'),
(9, 'PROFILE_DELETE', 'Supprimer un profil', 'Permet de supprimer des profils', 'PROFILE'),

-- Product Module
(10, 'PRODUCT_VIEW', 'Voir les produits', 'Permet de consulter le catalogue de produits', 'PRODUCT'),
(11, 'PRODUCT_CREATE', 'Créer un produit', 'Permet de créer de nouveaux produits', 'PRODUCT'),
(12, 'PRODUCT_UPDATE', 'Modifier un produit', 'Permet de modifier les produits', 'PRODUCT'),
(13, 'PRODUCT_DELETE', 'Supprimer un produit', 'Permet de supprimer des produits', 'PRODUCT'),

-- Gamme Module
(14, 'GAMME_VIEW', 'Voir les gammes', 'Permet de consulter les gammes de produits', 'GAMME'),
(15, 'GAMME_CREATE', 'Créer une gamme', 'Permet de créer de nouvelles gammes', 'GAMME'),
(16, 'GAMME_UPDATE', 'Modifier une gamme', 'Permet de modifier les gammes', 'GAMME'),
(17, 'GAMME_DELETE', 'Supprimer une gamme', 'Permet de supprimer des gammes', 'GAMME'),

-- Etablissement Module
(18, 'ETABLISSEMENT_VIEW', 'Voir les établissements', 'Permet de consulter les établissements', 'ETABLISSEMENT'),
(19, 'ETABLISSEMENT_CREATE', 'Créer un établissement', 'Permet de créer de nouveaux établissements', 'ETABLISSEMENT'),
(20, 'ETABLISSEMENT_UPDATE', 'Modifier un établissement', 'Permet de modifier les établissements', 'ETABLISSEMENT'),
(21, 'ETABLISSEMENT_DELETE', 'Supprimer un établissement', 'Permet de supprimer des établissements', 'ETABLISSEMENT'),

-- Depot Module
(22, 'DEPOT_VIEW', 'Voir les dépôts', 'Permet de consulter les dépôts', 'DEPOT'),
(23, 'DEPOT_CREATE', 'Créer un dépôt', 'Permet de créer de nouveaux dépôts', 'DEPOT'),
(24, 'DEPOT_UPDATE', 'Modifier un dépôt', 'Permet de modifier les dépôts', 'DEPOT'),
(25, 'DEPOT_DELETE', 'Supprimer un dépôt', 'Permet de supprimer des dépôts', 'DEPOT'),

-- Zone Module
(26, 'ZONE_VIEW', 'Voir les zones', 'Permet de consulter les zones', 'ZONE'),
(27, 'ZONE_CREATE', 'Créer une zone', 'Permet de créer de nouvelles zones', 'ZONE'),
(28, 'ZONE_UPDATE', 'Modifier une zone', 'Permet de modifier les zones', 'ZONE'),
(29, 'ZONE_DELETE', 'Supprimer une zone', 'Permet de supprimer des zones', 'ZONE'),

-- Location Module
(30, 'LOCATION_VIEW', 'Voir les emplacements', 'Permet de consulter les emplacements', 'LOCATION'),
(31, 'LOCATION_CREATE', 'Créer un emplacement', 'Permet de créer de nouveaux emplacements', 'LOCATION'),
(32, 'LOCATION_UPDATE', 'Modifier un emplacement', 'Permet de modifier les emplacements', 'LOCATION'),
(33, 'LOCATION_DELETE', 'Supprimer un emplacement', 'Permet de supprimer des emplacements', 'LOCATION'),

-- Dashboard Module
(34, 'DASHBOARD_VIEW', 'Voir le tableau de bord', 'Permet d\'accéder au tableau de bord', 'DASHBOARD'),
(35, 'DASHBOARD_ADMIN', 'Administration du tableau de bord', 'Permet d\'administrer le tableau de bord', 'DASHBOARD');

-- Profiles
INSERT INTO profiles (id, code, nom, description) VALUES
(1, 'ADMIN', 'Administrateur', 'Accès complet à toutes les fonctionnalités du système'),
(2, 'MANAGER', 'Gestionnaire', 'Gestion des produits et établissements, consultation des utilisateurs'),
(3, 'USER', 'Utilisateur', 'Consultation uniquement des produits et établissements');

-- Profile Permissions
-- ADMIN - All permissions
INSERT INTO profile_permissions (profile_id, permission_id) VALUES
-- User permissions
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
-- Profile permissions
(1, 6), (1, 7), (1, 8), (1, 9),
-- Product permissions
(1, 10), (1, 11), (1, 12), (1, 13),
-- Gamme permissions
(1, 14), (1, 15), (1, 16), (1, 17),
-- Etablissement permissions
(1, 18), (1, 19), (1, 20), (1, 21),
-- Depot permissions
(1, 22), (1, 23), (1, 24), (1, 25),
-- Zone permissions
(1, 26), (1, 27), (1, 28), (1, 29),
-- Location permissions
(1, 30), (1, 31), (1, 32), (1, 33),
-- Dashboard permissions
(1, 34), (1, 35);

-- MANAGER - Management permissions for products and establishments
INSERT INTO profile_permissions (profile_id, permission_id) VALUES
-- User permissions (view and assign)
(2, 1), (2, 5),
-- Profile permissions (view)
(2, 6),
-- Product permissions (full CRUD)
(2, 10), (2, 11), (2, 12), (2, 13),
-- Gamme permissions (full CRUD)
(2, 14), (2, 15), (2, 16), (2, 17),
-- Etablissement permissions (full CRUD)
(2, 18), (2, 19), (2, 20), (2, 21),
-- Depot permissions (full CRUD)
(2, 22), (2, 23), (2, 24), (2, 25),
-- Zone permissions (full CRUD)
(2, 26), (2, 27), (2, 28), (2, 29),
-- Location permissions (full CRUD)
(2, 30), (2, 31), (2, 32), (2, 33),
-- Dashboard permissions (view)
(2, 34);

-- USER - Read-only permissions
INSERT INTO profile_permissions (profile_id, permission_id) VALUES
-- Product permissions (view only)
(3, 10),
-- Gamme permissions (view only)
(3, 14),
-- Etablissement permissions (view only)
(3, 18),
-- Depot permissions (view only)
(3, 22),
-- Zone permissions (view only)
(3, 26),
-- Location permissions (view only)
(3, 30),
-- Dashboard permissions (view only)
(3, 34);

-- Users (passwords are hashed with BCrypt)
-- admin / admin123
INSERT INTO users (id, username, email, password, first_name, last_name, active, profile_id, created_at, updated_at) VALUES
(1, 'admin', 'admin@trace.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'System', true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- manager / manager123
INSERT INTO users (id, username, email, password, first_name, last_name, active, profile_id, created_at, updated_at) VALUES
(2, 'manager', 'manager@trace.com', '$2a$10$DkS9y3o5ww8yEbOEZNbYAeYoP5TP1Cg0FMxRaBE.U4yFVweDHWjDe', 'Manager', 'User', true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- user / user123
INSERT INTO users (id, username, email, password, first_name, last_name, active, profile_id, created_at, updated_at) VALUES
(3, 'user', 'user@trace.com', '$2a$10$3iFo5nV7nCh05a1hs.6YYOdRZxf54uMkjE/2fJF9h0pnprhKhfWvi', 'Standard', 'User', true, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
