package com.trace.user.service;

import com.trace.user.entity.Permission;
import com.trace.user.entity.Profile;
import com.trace.user.repository.PermissionRepository;
import com.trace.user.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private ProfileService profileService;

    private Profile profile;
    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setId(1L);
        permission.setCode("TEST_PERM");

        profile = Profile.builder()
                .id(1L)
                .code("ADMIN")
                .nom("Administrateur")
                .permissions(new ArrayList<>())
                .build();
    }

    @Test
    void findAll_ShouldReturnList() {
        when(profileRepository.findAll()).thenReturn(List.of(profile));
        List<Profile> result = profileService.findAll();
        assertThat(result).hasSize(1);
        verify(profileRepository, times(1)).findAll();
    }

    @Test
    void save_ShouldReturnSavedProfile() {
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);
        Profile result = profileService.save(profile);
        assertThat(result).isNotNull();
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    void addPermission_ShouldAddPermissionToProfile() {
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Profile result = profileService.addPermission(1L, 1L);

        assertThat(result.getPermissions()).contains(permission);
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    void removePermission_ShouldRemovePermissionFromProfile() {
        profile.getPermissions().add(permission);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Profile result = profileService.removePermission(1L, 1L);

        assertThat(result.getPermissions()).doesNotContain(permission);
        verify(profileRepository, times(1)).save(profile);
    }
}
