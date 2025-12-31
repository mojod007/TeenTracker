package com.trace.user.service;

import com.trace.user.entity.Permission;
import com.trace.user.entity.Profile;
import com.trace.user.repository.PermissionRepository;
import com.trace.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    
    private final ProfileRepository profileRepository;
    private final PermissionRepository permissionRepository;

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
    }

    public Profile findByCode(String code) {
        return profileRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Profile not found with code: " + code));
    }

    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }

    public Profile addPermission(Long profileId, Long permissionId) {
        Profile profile = findById(profileId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        if (!profile.getPermissions().contains(permission)) {
            profile.getPermissions().add(permission);
            return profileRepository.save(profile);
        }
        return profile;
    }

    public Profile removePermission(Long profileId, Long permissionId) {
        Profile profile = findById(profileId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        profile.getPermissions().remove(permission);
        return profileRepository.save(profile);
    }
}
