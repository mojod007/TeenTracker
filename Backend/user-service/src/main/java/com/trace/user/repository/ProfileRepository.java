package com.trace.user.repository;

import com.trace.user.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByCode(String code);

    @Query("SELECT p FROM Profile p LEFT JOIN FETCH p.users")
    List<Profile> findAllWithUsers();
}
