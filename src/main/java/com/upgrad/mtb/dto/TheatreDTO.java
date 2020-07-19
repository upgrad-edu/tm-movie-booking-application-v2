package com.upgrad.mtb.dto;

import com.upgrad.mtb.beans.Booking;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class TheatreDTO {
    int theatreId;
    String theatreName;
    int noOfSeats;
    int ticketPrice;
    int cityId;
    List<Integer> movieIds;
    List<Integer> bookingIds;
}
