package com.ianclark226.gamingvenue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String venueType;
    private BigDecimal venuePrice;
    private boolean isBooked = false;

    @Lob
    private Blob photo;
    @OneToMany(mappedBy = "venue",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<BookedVenue> bookings;

    public Venue() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedVenue booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);

        booking.setVenue(this);

        isBooked = true;

        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);

    }
}