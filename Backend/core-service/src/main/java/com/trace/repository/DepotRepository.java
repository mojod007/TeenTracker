package com.trace.repository;

import com.trace.entity.Depot;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {

    @EntityGraph(attributePaths = "zones")
    List<Depot> findAll();

    @EntityGraph(attributePaths = "zones")
    List<Depot> findByEtablissementId(Long etablissementId);
}
