package com.ianclark226.gamingvenue.controller;

import com.ianclark226.gamingvenue.exception.PhotoRetrievalException;
import com.ianclark226.gamingvenue.exception.ResourceNotFoundException;
import com.ianclark226.gamingvenue.model.BookedVenue;
import com.ianclark226.gamingvenue.model.Venue;
import com.ianclark226.gamingvenue.response.BookingResponse;
import com.ianclark226.gamingvenue.response.VenueResponse;
import com.ianclark226.gamingvenue.service.BookingService;
import com.ianclark226.gamingvenue.service.IVenueService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/venues")
public class VenueController {
    private final IVenueService venueService;
    private final BookingService bookingService;

    @PostMapping("/add/new-venue")

    public ResponseEntity<VenueResponse> addNewVenue(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("venueType") String venueType,
            @RequestParam("venuePrice") BigDecimal venuePrice) throws SQLException, IOException {

        Venue savedVenue = venueService.addNewVenue(photo, venueType, venuePrice);
        VenueResponse response = new VenueResponse(savedVenue.getId(), savedVenue.getVenueType(), savedVenue.getVenuePrice());

        return ResponseEntity.ok(response);



    }

    @GetMapping("/venue/types")
    public List<String> getVenueTypes() {
        return venueService.getAllVenueTypes();
    }

    @GetMapping("/all-venues")
    public ResponseEntity<List<VenueResponse>> getAllVenues() throws SQLException {
        List<Venue> venues = venueService.getAllVenues();
        List<VenueResponse> venueResponses = new ArrayList<>();

        for(Venue venue : venues) {
            byte[] photoBytes = venueService.getVenuePhotoByVenueId(venue.getId());
            if(photoBytes != null && photoBytes.length > 0) {
                // Might cause an error
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                VenueResponse venueResponse = getVenueResponse(venue);
                venueResponse.setPhoto(base64Photo);
                venueResponses.add(venueResponse);
            }
        }
        return ResponseEntity.ok(venueResponses);
    }

    @DeleteMapping("/delete/venue/{venueId}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long venueId) {
        venueService.deleteVenue(venueId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{venueId}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long venueId,
                                                     @RequestParam(required = false) String venueType,
                                                     @RequestParam(required = false) BigDecimal venuePrice,
                                                     @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : venueService.getVenuePhotoByVenueId(venueId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Venue theVenue = venueService.updateVenue(venueId, venueType, venuePrice, photoBytes);
        theVenue.setPhoto(photoBlob);
        VenueResponse venueResponse = getVenueResponse(theVenue);
        return ResponseEntity.ok(venueResponse);

    }

    @GetMapping("/venue/{venueId}")
    public ResponseEntity<Optional<VenueResponse>> getVenueById(@PathVariable Long venueId) {
        Optional<Venue> theVenue = venueService.getVenueById(venueId);
        return theVenue.map(venue -> {
            VenueResponse venueResponse = getVenueResponse(venue);
            return ResponseEntity.ok(Optional.of(venueResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Venue not found"));

    }

    private VenueResponse getVenueResponse(Venue venue) {
        List<BookedVenue> bookings = getAllBookingsByVenueId(venue.getId());
        List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getBookingId(),
                        booking.getStart_Date(),
                        booking.getEnd_Date(),
                        booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = venue.getPhoto();
        if(photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());

            } catch(SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new VenueResponse(venue.getId(),
                venue.getVenueType(),
                venue.getVenuePrice(),
                venue.isBooked(), photoBytes, bookingInfo);
    }

    private List<BookedVenue> getAllBookingsByVenueId(Long venueId) {
        return bookingService.getAllBookingsByVenueId(venueId);
    }
}
