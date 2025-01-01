package com.ianclark226.gamingvenue.repo;


import com.ianclark226.gamingvenue.model.BookedVenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepo extends JpaRepository<BookedVenue, Long> {
    BookedVenue findByBookingConfirmationCode(String confirmationCode);

    List<BookedVenue> findByVenueId(Long venueId);
}
