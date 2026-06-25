package com.trace.repository;

import com.trace.entity.Zone;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @EntityGraph(attributePaths = "locations")
    List<Zone> findAll();

    @EntityGraph(attributePaths = "locations")
    List<Zone> findByDepotId(Long depotId);
}
