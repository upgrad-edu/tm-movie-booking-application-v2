package com.upgrad.mtb.dto;

import lombok.Data;

import java.util.List;

@Data
public class TheatreDTO {
    int theatreId;
    String theatreName;
    int ticketPrice;
    int cityId;
    List<Integer> movieIds;
    List<Integer> bookingIds;
}
