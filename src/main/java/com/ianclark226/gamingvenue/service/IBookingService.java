package com.ianclark226.gamingvenue.service;

import com.ianclark226.gamingvenue.model.BookedVenue;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long venueId, BookedVenue bookingRequest);

    BookedVenue findByBookingConfirmationCode(String confirmationCode);

    List<BookedVenue> getAllBookings();
}
