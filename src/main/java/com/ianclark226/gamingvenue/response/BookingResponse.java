package com.ianclark226.gamingvenue.response;

import com.ianclark226.gamingvenue.model.Venue;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long bookingId;

    private Date start_Date;

    private Date end_Date;

    private String organizerFullName;

    private String organizerEmail;

    private int numOfOrganizers;

    private int numOfEvents;

    private int totalNumOfOrganizers;

    private String bookingConfirmationCode;

    private Venue venue;

    public BookingResponse(Long bookingId, Date start_Date, Date end_Date, String bookingConfirmationCode) {
        this.bookingId = bookingId;
        this.start_Date = start_Date;
        this.end_Date = end_Date;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
