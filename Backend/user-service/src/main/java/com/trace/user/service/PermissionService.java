package com.trace.user.service;

import com.trace.user.entity.Permission;
import com.trace.user.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionService {
    
    private final PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    public Permission findByCode(String code) {
        return permissionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Permission not found with code: " + code));
    }

    public List<Permission> findByModule(String module) {
        return permissionRepository.findByModule(module);
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }
}
