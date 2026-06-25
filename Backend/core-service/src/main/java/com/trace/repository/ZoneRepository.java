package com.trace.repository;

import com.trace.entity.Zone;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @EntityGraph(attributePaths = {"depot", "depot.etablissement"})
    Optional<Zone> findById(Long id);

    @EntityGraph(attributePaths = {"depot", "depot.etablissement"})
    List<Zone> findAll();

    @EntityGraph(attributePaths = {"depot", "depot.etablissement"})
    List<Zone> findByDepotId(Long depotId);
}
