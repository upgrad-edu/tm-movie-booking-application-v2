package com.upgrad.mtb.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CustomerDTO {
    int customerId;
    String firstName;
    String lastName;
    Date dateOfBirth;
    String username;
    String password;
    int userTypeId;
    String jwtToken;
    List<String> phoneNumbers;
    List<Integer> bookingIds;
}
