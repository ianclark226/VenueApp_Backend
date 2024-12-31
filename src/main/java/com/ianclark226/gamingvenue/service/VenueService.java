package com.ianclark226.gamingvenue.service;

import com.ianclark226.gamingvenue.exception.ResourceNotFoundException;
import com.ianclark226.gamingvenue.model.Venue;
import com.ianclark226.gamingvenue.repo.VenueRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class VenueService implements IVenueService {
    private final VenueRepo venueRepo;
    @Override
    public Venue addNewVenue(MultipartFile file, String venueType, BigDecimal venuePrice) throws SQLException, IOException {
        Venue venue = new Venue();
        venue.setVenueType(venueType);
        venue.setVenuePrice(venuePrice);
        if(!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            venue.setPhoto(photoBlob);
        }
        return venueRepo.save(venue);
    }

    @Override
    public List<String> getAllVenueTypes() {
        return venueRepo.findDistinctVenueTypes();
    }

    @Override
    public List<Venue> getAllVenues() {
        return venueRepo.findAll();
    }

    @Override
    public byte[] getVenuePhotoByVenueId(Long venueId) throws SQLException {
        Optional<Venue> theVenue = venueRepo.findById(venueId);
        if(theVenue.isEmpty()) {
            throw new ResourceNotFoundException("Apologies, Venue not Found.");
        }
        Blob photoBlob = theVenue.get().getPhoto();
        if(photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }
}
