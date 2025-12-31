package com.trace.product.service;

import com.trace.product.entity.Gamme;
import com.trace.product.repository.GammeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GammeService {
    private final GammeRepository gammeRepository;

    public List<Gamme> getAllGammes() {
        return gammeRepository.findAll();
    }

    public Optional<Gamme> getGammeById(Long id) {
        return gammeRepository.findById(id);
    }

    public Gamme saveGamme(Gamme gamme) {
        return gammeRepository.save(gamme);
    }

    public void deleteGamme(Long id) {
        gammeRepository.deleteById(id);
    }

    public Optional<Gamme> getGammeByCode(String code) {
        return gammeRepository.findByCode(code);
    }
}
