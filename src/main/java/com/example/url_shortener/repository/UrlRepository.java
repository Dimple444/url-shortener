package com.example.url_shortener.repository;

import com.example.url_shortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    @Query(value = "SELECT next_val('url_sequence')", nativeQuery = true)
    Long getNextSequenceValue();
}
