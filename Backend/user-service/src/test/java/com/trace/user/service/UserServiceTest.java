package com.trace.user.service;

import com.trace.user.entity.User;
import com.trace.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .active(true)
                .build();
    }

    @Test
    void save_ShouldEncodePasswordAndSaveUser_WhenNewUser() {
        // Given
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.save(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void save_ShouldNotEncodePassword_WhenPasswordAlreadyEncoded() {
        // Given
        user.setPassword("$2a$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.save(user);

        // Then
        assertThat(result).isNotNull();
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void findById_ShouldThrowException_WhenNotExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur non trouvé avec l'ID: 1");
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete_WhenUserExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteById(1L);

        // Then
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenUserNotExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Utilisateur non trouvé avec l'ID: 1");
    }
}
