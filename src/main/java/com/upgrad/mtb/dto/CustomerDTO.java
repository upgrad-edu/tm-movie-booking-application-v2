package com.upgrad.mtb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
public class CustomerDTO {
    int customerId;
    String firstName;
    String lastName;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date dateOfBirth;
    String username;
    String password;
    int userTypeId;
    String jwtToken;
    List<String> phoneNumbers;
    List<Integer> bookingIds;
}
