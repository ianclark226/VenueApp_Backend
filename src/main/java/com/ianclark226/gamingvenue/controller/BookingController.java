package com.ianclark226.gamingvenue.controller;


import com.ianclark226.gamingvenue.exception.InvalidBookingRequestException;
import com.ianclark226.gamingvenue.exception.ResourceNotFoundException;
import com.ianclark226.gamingvenue.model.BookedVenue;
import com.ianclark226.gamingvenue.model.Venue;
import com.ianclark226.gamingvenue.response.BookingResponse;
import com.ianclark226.gamingvenue.response.VenueResponse;
import com.ianclark226.gamingvenue.service.IBookingService;
import com.ianclark226.gamingvenue.service.IVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final IBookingService bookingService;
    private final IVenueService venueService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedVenue> bookings = bookingService.getAllBookings();
        System.out.println("===bookings===");
        System.out.println(bookings);

        List<BookingResponse> BookingResponses = new ArrayList<>();
        for (BookedVenue booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            BookingResponses.add(bookingResponse);
        }

        return ResponseEntity.ok(BookingResponses);
    }

    @PostMapping("/venue/{venueId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long venueId,
                                         @RequestBody BookedVenue bookingRequest) {
        try {
            String confirmationCode = bookingService.saveBooking(venueId, bookingRequest);
            return ResponseEntity.ok("Venue booked successfully, Your booking confirmation code is :" + confirmationCode);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode) {
        try {
            BookedVenue booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedVenue booking) {
        Venue venue = venueService.getVenueById(booking.getVenue().getId()).get();
        VenueResponse venueResponse = new VenueResponse(
                venue.getId(),
                venue.getVenueType(),
                venue.getVenuePrice()
        );

        return new BookingResponse(
                booking.getBookingId(),
                booking.getStart_Date(),
                booking.getEnd_Date(),
                booking.getOrganizerFullName(),
                booking.getOrganizerEmail(),
                booking.getNumberOfOrganizers(),
                booking.getNumberOfEvents(),
                booking.getTotalNumOfOrganizers(),
                booking.getBookingConfirmationCode(),
                venueResponse
        );
    }

}
