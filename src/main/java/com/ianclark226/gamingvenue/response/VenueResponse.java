package com.ianclark226.gamingvenue.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor


public class VenueResponse {

    private Long id;
    private String venueType;
    private BigDecimal venuePrice;
    private Boolean isBooked;
    private String photo;
    private List<BookingResponse> bookings;

    public VenueResponse(Long id, String venueType, BigDecimal venuePrice) {
        this.id = id;
        this.venueType = venueType;
        this.venuePrice = venuePrice;
    }

    public VenueResponse(Long id, String venueType, BigDecimal venuePrice, Boolean isBooked, byte[] photoBytes, List<BookingResponse> bookings) {
        this.id = id;
        this.venueType = venueType;
        this.venuePrice = venuePrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
        this.bookings = bookings;
    }
}
