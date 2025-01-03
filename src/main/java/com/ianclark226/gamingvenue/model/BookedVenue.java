package com.ianclark226.gamingvenue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate start_Date;

    @Column(name="end_Date")
    private LocalDate end_Date;

    @Column(name="organizer_full_name")
    private String organizerFullName;

    @Column(name="organizer_email")
    private String organizerEmail;

    @Column(name="organizers")
    private int numberOfOrganizers;

    @Column(name="events")
    private int numberOfEvents;

    @Column(name="total_organizers")
    private int totalNumOfOrganizers;

    @Column(name="confirmation_Code")
    private String bookingConfirmationCode;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public void calculateTotalNumberOfGuest(){
        this.totalNumOfOrganizers = this.numberOfOrganizers + numberOfEvents;
    }

    public void setNumofOrganizer(int numOfAdults) {
        this.numberOfOrganizers = numOfAdults;
        calculateTotalNumberOfGuest();
    }

    public void setNumOfChildren(int numOfChildren) {
        this.numberOfEvents = numOfChildren;
        calculateTotalNumberOfGuest();
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
