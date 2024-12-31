package com.ianclark226.gamingvenue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long bookingId;

    @Column(name = "start_Date")
    private Date start_Date;

    @Column(name = "end_Date")
    private Date end_Date;

    @Column(name = "organizer_FullName")
    private String organizerFullName;

    @Column(name = "organizer_Email")
    private String organizerEmail;

    @Column(name = "organizers")
    private int numOfOrganizers;

    @Column(name = "events")
    private int numOfEvents;

    @Column(name = "total_organizers")
    private int totalNumOfOrganizers;

    @Column(name = "confirmation_code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;



    public void calculateTotal() {
        this.totalNumOfOrganizers = this.numOfOrganizers * this.numOfEvents;
    }

    public void setNumOfEvents(int numOfEvents) {
        this.numOfEvents = numOfEvents;
        calculateTotal();
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfOrganizers = numOfAdults;
        calculateTotal();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
