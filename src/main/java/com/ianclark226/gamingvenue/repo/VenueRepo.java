package com.ianclark226.gamingvenue.repo;

import com.ianclark226.gamingvenue.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueRepo extends JpaRepository<Venue, Long> {

    @Query("SELECT DISTINCT v.venueType FROM Venue v")

    List<String> findDistinctVenueTypes();
}
