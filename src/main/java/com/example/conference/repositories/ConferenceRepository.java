package com.example.conference.repositories;

import com.example.conference.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {


    @Query("FROM Conference c LEFT JOIN FETCH c.talks t WHERE c.id = ?1")
    Optional<Conference> findByIdPrefetchTalks(Long id);
}