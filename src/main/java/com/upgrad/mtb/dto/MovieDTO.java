package com.upgrad.mtb.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
public class MovieDTO {
    int movieId;
    String coverURL;
    String description;
    int duration;
    String name;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date releaseDate;
    String trailerURL;
    int languageId;
    int statusId;
    List<Integer> theatreIds;
}
