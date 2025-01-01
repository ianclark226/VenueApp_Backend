package com.ianclark226.gamingvenue.controller;


import com.ianclark226.gamingvenue.exception.InvalidBookingRequestException;
import com.ianclark226.gamingvenue.exception.ResourceNotFoundException;
import com.ianclark226.gamingvenue.model.BookedVenue;
import com.ianclark226.gamingvenue.model.Venue;
import com.ianclark226.gamingvenue.response.BookingResponse;
import com.ianclark226.gamingvenue.response.VenueResponse;
import com.ianclark226.gamingvenue.service.IBookingService;
import com.ianclark226.gamingvenue.service.IVenueService;
import com.ianclark226.gamingvenue.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IVenueService venueService;

    @GetMapping("all-bookings")

    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedVenue> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedVenue booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }

        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedVenue booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);

            return ResponseEntity.ok(bookingResponse);

        } catch(ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/venue/{venueId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long venueId,
                                         @RequestBody BookedVenue bookingRequest) {

        try {
            String confirmationCode = bookingService.saveBooking(venueId, bookingRequest);
            return ResponseEntity.ok("Venue Booked Successfully, Your booking confirmation code is:" + confirmationCode);

        } catch(InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedVenue booking) {
        Venue theVenue = venueService.getVenueById(booking.getVenue().getId()).get();
        VenueResponse venue = new VenueResponse(
                theVenue.getId(),
                theVenue.getVenueType(),
                theVenue.getVenuePrice());

        return new BookingResponse(booking.getBookingId(),
                booking.getStart_Date(),
                booking.getEnd_Date(),
                booking.getOrganizerFullName(),
                booking.getOrganizerEmail(),
                booking.getNumOfOrganizers(),
                booking.getNumOfEvents(),
                booking.getTotalNumOfOrganizers(),
                booking.getBookingConfirmationCode(),
                venue);
    }
}
