package com.trace.backend.repository;

import com.trace.backend.entity.UserAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAssignmentRepository extends JpaRepository<UserAssignment, Long> {

    @Modifying
    @Query("DELETE FROM UserAssignment ua WHERE ua.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT ua.etablissement.id FROM UserAssignment ua WHERE ua.userId = :userId AND ua.etablissement IS NOT NULL")
    List<Long> findEtablissementIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT ua.depot.id FROM UserAssignment ua WHERE ua.userId = :userId AND ua.depot IS NOT NULL")
    List<Long> findDepotIdsByUserId(@Param("userId") Long userId);
}
