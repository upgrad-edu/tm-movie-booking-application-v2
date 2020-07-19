package com.upgrad.mtb.utils;

import com.upgrad.mtb.beans.Booking;
import com.upgrad.mtb.beans.Customer;
import com.upgrad.mtb.beans.Movie;
import com.upgrad.mtb.beans.Theatre;
import com.upgrad.mtb.dto.BookingDTO;
import com.upgrad.mtb.dto.CustomerDTO;
import com.upgrad.mtb.dto.MovieDTO;
import com.upgrad.mtb.dto.TheatreDTO;
import com.upgrad.mtb.exceptions.*;
import com.upgrad.mtb.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DTOEntityConverter {

    @Autowired
    CustomerService customerService;
    @Autowired
    TheatreService theatreService;
    @Autowired
    CityService cityService;
    @Autowired
    MovieService movieService;
    @Autowired
    LanguageService languageService;
    @Autowired
    StatusService statusService;
    @Autowired
    UserTypeService userTypeService;
    @Autowired
    BookingService bookingService;

    public Movie convertToMovieEntity(MovieDTO movieDTO) throws StatusDetailsNotFoundException, LanguageDetailsNotFoundException, TheatreDetailsNotFoundException, CustomerDetailsNotFoundException, MovieDetailsNotFoundException {
        Movie movie = new Movie();
        movie.setCoverPhotoURL(movieDTO.getCoverURL());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setDuration(movieDTO.getDuration());
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setTrailerURL(movieDTO.getTrailerURL());
        movie.setStatus(statusService.getStatusDetails(movieDTO.getStatusId()));
        List<Theatre> theatreList = new ArrayList<>();
        if(movieDTO.getTheatreIds() != null) {
            for (Integer theatreId : movieDTO.getTheatreIds()) {
                theatreList.add(theatreService.getTheatreDetails(theatreId));
            }
        }
        movie.setLanguage(languageService.getLanguageDetails(movieDTO.getLanguageId()));
        movie.setTheatres(theatreList);
        return movie;
    }

    public Theatre convertToTheatreEntity(TheatreDTO theatreDTO) throws TheatreDetailsNotFoundException, CustomerDetailsNotFoundException, MovieDetailsNotFoundException, LanguageDetailsNotFoundException, StatusDetailsNotFoundException, BookingDetailsNotFoundException {
        Theatre theatre = new Theatre();
        theatre.setTicketPrice(theatreDTO.getTicketPrice());
        theatre.setNoOfSeats(theatreDTO.getNoOfSeats());
        theatre.setTheatreName(theatreDTO.getTheatreName());
        theatre.setCity(cityService.getCityDetails(theatreDTO.getCityId()));
        List<Movie> movieList = new ArrayList<>();
        if(theatreDTO.getMovieIds() != null) {
            for (Integer movieId : theatreDTO.getMovieIds()) {
                movieList.add(movieService.getMovieDetails(movieId));
            }
            theatre.setMovies(movieList);
        }
        List<Booking> bookingList = new ArrayList<>();
        if(theatreDTO.getBookingIds() != null) {
            for (Integer bookingId : theatreDTO.getBookingIds()) {
                bookingList.add(bookingService.getBookingDetails(bookingId));
            }
            theatre.setBookings(bookingList);
        }
        return theatre;
    }

    public Booking convertToBookingEntity(BookingDTO bookingDTO) throws CustomerDetailsNotFoundException, TheatreDetailsNotFoundException {
        Booking booking = new Booking();
        booking.setNoOfSeats(bookingDTO.getNoOfSeats());
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setCustomer(customerService.getCustomerDetails(bookingDTO.getCustomerId()));
        booking.setTheatre(theatreService.getTheatreDetails(bookingDTO.getTheatreId()));
        return booking;
    }

    public Customer convertToCustomerEntity(CustomerDTO customerDTO) throws UserTypeDetailsNotFoundException, TheatreDetailsNotFoundException, CustomerDetailsNotFoundException, BookingDetailsNotFoundException {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setUsername(customerDTO.getUsername());
        customer.setPassword(customerDTO.getPassword());
        customer.setPhoneNumbers(customerDTO.getPhoneNumbers());
        System.out.println(customerDTO.getUserTypeId());
        System.out.println(userTypeService.getUserTypeDetails(customerDTO.getUserTypeId()));
        customer.setUserType(userTypeService.getUserTypeDetails(customerDTO.getUserTypeId()));
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        List<Booking> bookings = new ArrayList<>();
        if(customerDTO.getBookingIds() != null) {
            for (Integer bookingId : customerDTO.getBookingIds())
                bookings.add(bookingService.getBookingDetails(bookingId));
            customer.setBookings(bookings);
        }
        return customer;
    }
}
