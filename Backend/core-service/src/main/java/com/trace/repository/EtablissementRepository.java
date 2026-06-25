package com.trace.repository;

import com.trace.entity.Etablissement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {

    @EntityGraph(attributePaths = "depots")
    List<Etablissement> findAll();

    @EntityGraph(attributePaths = "depots")
    Optional<Etablissement> findById(Long id);
}
