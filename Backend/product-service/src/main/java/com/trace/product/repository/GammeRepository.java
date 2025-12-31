package com.trace.product.repository;

import com.trace.product.entity.Gamme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GammeRepository extends JpaRepository<Gamme, Long> {
    Optional<Gamme> findByCode(String code);
}
