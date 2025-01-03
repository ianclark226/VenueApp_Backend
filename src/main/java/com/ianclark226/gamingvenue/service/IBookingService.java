package com.ianclark226.gamingvenue.service;

import com.ianclark226.gamingvenue.model.BookedVenue;

import java.util.List;

public interface IBookingService {

    List<BookedVenue> getAllBookings();
    List<BookedVenue> getAllBookingsByVenueId(Long venueId);
    String saveBooking(Long venueId, BookedVenue bookingRequest);
    BookedVenue findByBookingConfirmationCode(String confirmationCode);
    void cancelBooking(Long bookingId);
}
