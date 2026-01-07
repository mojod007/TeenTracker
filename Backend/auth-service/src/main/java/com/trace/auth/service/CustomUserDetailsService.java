package com.trace.auth.service;

import com.trace.auth.entity.User;
import com.trace.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final org.springframework.web.client.RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userServiceUrl = "http://user-service/api/users/username/" + username;
        User user;
        try {
            user = restTemplate.getForObject(userServiceUrl, User.class);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Erreur de communication avec le service utilisateur: " + username, e);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé dans user-service: " + username);
        }

        List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
        if (user.getProfile() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getProfile().getNom()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
