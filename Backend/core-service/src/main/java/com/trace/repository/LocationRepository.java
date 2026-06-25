package com.trace.repository;

import com.trace.entity.Location;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @EntityGraph(attributePaths = {"zone", "zone.depot", "zone.depot.etablissement"})
    List<Location> findByZoneId(Long zoneId);
}
