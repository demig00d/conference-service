package com.example.conference.repositories;

import com.example.conference.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l INNER JOIN l.conferences c WHERE c.id = ?1")
    List<Location> findAllByConferenceId(Long id);
}