package com.trace.backend.repository;

import com.trace.backend.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {
    List<Depot> findByEtablissementId(Long etablissementId);
}
