package com.ianclark226.gamingvenue.service;

import com.ianclark226.gamingvenue.model.Venue;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IVenueService {
    Venue addNewVenue(MultipartFile photo, String venueType, BigDecimal venuePrice) throws SQLException, IOException;

    List<String> getAllVenueTypes();

    List<Venue> getAllVenues();

    byte[] getVenuePhotoByVenueId(Long venueId) throws SQLException;

    void deleteVenue(Long venueId);

    Venue updateVenue(Long venueId, String venueType, BigDecimal venuePrice, byte[] photoBytes);

    Optional<Venue> getVenueById(Long venueId);
}
