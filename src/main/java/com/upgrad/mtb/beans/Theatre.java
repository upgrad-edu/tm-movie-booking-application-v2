package com.upgrad.mtb.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Theatre {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "theatre_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;
    @Column( nullable = false, unique = true)
    private String theatreName;
    @Column( nullable = false)
    private int noOfSeats;
    @Column( nullable = false)
    private int ticketPrice;

    @ManyToOne
    @JsonBackReference("city_theatre")
    private City city;

    @OneToMany(mappedBy = "theatre" , fetch = FetchType.LAZY)
    @MapKey
    @JsonManagedReference("theatre_booking")
    List<Booking> bookings;

    @ManyToMany(mappedBy = "theatres" , fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JsonBackReference("movie_theatre")
    private List<Movie> movies;

    public Theatre(){}

    public Theatre(String theatreName, int noOfSeats, int ticketPrice, City city, List<Booking> bookings, List<Movie> movies) {
        this.theatreName = theatreName;
        this.noOfSeats = noOfSeats;
        this.ticketPrice = ticketPrice;
        this.city = city;
        this.bookings = bookings;
        this.movies = movies;
    }
}
