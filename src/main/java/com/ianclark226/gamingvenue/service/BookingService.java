package com.ianclark226.gamingvenue.service;


import com.ianclark226.gamingvenue.exception.InvalidBookingRequestException;
import com.ianclark226.gamingvenue.exception.ResourceNotFoundException;
import com.ianclark226.gamingvenue.model.BookedVenue;
import com.ianclark226.gamingvenue.model.Venue;
import com.ianclark226.gamingvenue.repo.BookingRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepo bookingRepo;
    private final IVenueService venueService;

    @Override
    public List<BookedVenue> getAllBookings() {
        return bookingRepo.findAll();
    }

    @Override
    public List<BookedVenue> getAllBookingsByVenueId(Long venueId) {
        return bookingRepo.findByVenueId(venueId);
    }

    @Override
    public String saveBooking(Long roomId, BookedVenue bookingRequest) {
        if (bookingRequest.getEnd_Date().isBefore(bookingRequest.getStart_Date())) {
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }

        Venue venue = venueService.getVenueById(roomId).get();
        List<BookedVenue> existingBookings = venue.getBookings();
        boolean venueIsAvailable = venueIsAvailable(bookingRequest, existingBookings);

        if (venueIsAvailable) {
            venue.addBooking(bookingRequest);
            bookingRepo.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }

        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedVenue findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepo.findByBookingConfirmationCode(confirmationCode);

    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepo.deleteById(bookingId);
    }

    private boolean venueIsAvailable(BookedVenue bookingRequest, List<BookedVenue> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                bookingRequest.getStart_Date().equals(existingBooking.getStart_Date())
                        || bookingRequest.getEnd_Date().isBefore(existingBooking.getEnd_Date())

                        || (bookingRequest.getStart_Date().isAfter(existingBooking.getStart_Date())
                        && bookingRequest.getStart_Date().isBefore(existingBooking.getEnd_Date()))

                        || (bookingRequest.getStart_Date().isBefore(existingBooking.getStart_Date())
                        && bookingRequest.getEnd_Date().equals(existingBooking.getEnd_Date()))

                        || (bookingRequest.getStart_Date().isBefore(existingBooking.getStart_Date())
                        && bookingRequest.getEnd_Date().isAfter(existingBooking.getEnd_Date()))

                        || (bookingRequest.getStart_Date().equals(existingBooking.getEnd_Date())
                        && bookingRequest.getEnd_Date().equals(existingBooking.getStart_Date()))

                        || (bookingRequest.getStart_Date().equals(existingBooking.getEnd_Date())
                        && bookingRequest.getEnd_Date().equals(bookingRequest.getStart_Date()))
        );
    }
}