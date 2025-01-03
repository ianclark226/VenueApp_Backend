package com.ianclark226.gamingvenue.response;

import com.ianclark226.gamingvenue.model.Venue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String organizerFullName;

    private String organizerEmail;

    private int numOfOrganizers;

    private int numOfEvents;

    private int totalNumOfOrganizers;

    private String bookingConfirmationCode;

    private VenueResponse room;

    public BookingResponse(Long id, LocalDate startDate,
                           LocalDate endDate, String bookingConfirmationCode) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
