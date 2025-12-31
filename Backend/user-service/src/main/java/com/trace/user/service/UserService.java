package com.trace.user.service;

import com.trace.user.entity.User;
import com.trace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> findAll() {
        log.info("Récupération de tous les utilisateurs");
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable) {
        log.info("Récupération des utilisateurs avec pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable);
    }

    public User findById(Long id) {
        log.info("Recherche de l'utilisateur avec ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
    }

    public User findByUsername(String username) {
        log.info("Recherche de l'utilisateur avec nom d'utilisateur: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec le nom d'utilisateur: " + username));
    }

    public User save(User user) {
        log.info("Sauvegarde de l'utilisateur: {}", user.getUsername());
        // Hash password if it's a new user or password has changed
        if (user.getId() == null || !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        log.info("Suppression de l'utilisateur avec ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User toggleActive(Long id) {
        log.info("Changement du statut actif pour l'utilisateur ID: {}", id);
        User user = findById(id);
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }

    public void changePassword(Long id, String newPassword) {
        log.info("Changement du mot de passe pour l'utilisateur ID: {}", id);
        User user = findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
