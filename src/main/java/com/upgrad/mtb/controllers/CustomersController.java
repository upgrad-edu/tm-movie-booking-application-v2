package com.upgrad.mtb.controllers;

import com.upgrad.mtb.beans.Booking;
import com.upgrad.mtb.beans.Customer;
import com.upgrad.mtb.dto.BookingDTO;
import com.upgrad.mtb.dto.CustomerDTO;
import com.upgrad.mtb.exceptions.BookingDetailsNotFoundException;
import com.upgrad.mtb.exceptions.CustomerDetailsNotFoundException;
import com.upgrad.mtb.exceptions.TheatreDetailsNotFoundException;
import com.upgrad.mtb.exceptions.UserTypeDetailsNotFoundException;
import com.upgrad.mtb.security.jwt.JwtTokenProvider;
import com.upgrad.mtb.services.BookingService;
import com.upgrad.mtb.services.CustomerServiceImpl;
import com.upgrad.mtb.utils.DTOEntityConverter;
import com.upgrad.mtb.utils.EntityDTOConverter;
import com.upgrad.mtb.validator.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
public class CustomersController {
    @Autowired
    CustomerServiceImpl customerService;

    @Autowired
    BookingService bookingService;
    @Autowired
    CustomerValidator customerValidator;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    EntityDTOConverter entityDTOConverter;
    @Autowired
    DTOEntityConverter dtoEntityConverter;
    
    @RequestMapping(value= {"/sayHelloCustomer"},method= RequestMethod.GET)
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<String>("Hello World To All From CustomerController", HttpStatus.OK);
    }


    @GetMapping("/customers/{id}")
    @ResponseBody
    public ResponseEntity getCustomerDetails(@PathVariable(name = "id") int id) throws CustomerDetailsNotFoundException {
        System.out.println("get customer details controller");
        Customer customer = customerService.getCustomerDetails(id);
        CustomerDTO responseCustomerDTO = entityDTOConverter.convertToCustomerDTO(customer);
        return  ResponseEntity.ok(responseCustomerDTO);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity updateCustomerDetails(@PathVariable(name = "id") int id , @RequestBody CustomerDTO customerDTO , @RequestHeader(value = "X-Access-Token") String accessToken) throws CustomerDetailsNotFoundException, UserTypeDetailsNotFoundException, TheatreDetailsNotFoundException, BookingDetailsNotFoundException {
        Customer newCustomer = dtoEntityConverter.convertToCustomerEntity(customerDTO);
        Customer updatedCustomer =  customerService.updateCustomerDetails(id, newCustomer);
        CustomerDTO updatedCustomerDTO = entityDTOConverter.convertToCustomerDTO(updatedCustomer);
        return ResponseEntity.ok(updatedCustomerDTO);
    }

    @GetMapping(value="/customers/{customerId}/bookings",produces=MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
    public ResponseEntity getAllBookingsForCustomer(@PathVariable("customerId") int id) throws CustomerDetailsNotFoundException {
        Customer customer = customerService.getCustomerDetails(id);
        List<Booking> bookings = customer.getBookings();
        List<BookingDTO> bookingDTOList = new ArrayList<>();
        for(Booking booking : bookings){
            bookingDTOList.add(entityDTOConverter.convertToBookingDTO(booking));
        }
        return  ResponseEntity.ok(bookingDTOList);
    }

    @DeleteMapping(value="/customers/{customerId}/bookings",produces=MediaType.APPLICATION_JSON_VALUE,headers="Accept=application/json")
    public ResponseEntity<String> deleteBookingForCustomer(@PathVariable("customerId") int customerId) throws CustomerDetailsNotFoundException, BookingDetailsNotFoundException {
        Customer customer = customerService.getCustomerDetails(customerId);
        List<Booking> bookings =  customer.getBookings();
        for(Booking booking : bookings){
            bookingService.deleteBooking(booking.getId());
            System.out.println("Booking deleted : " + booking.getId());
        }
        customer.setBookings(new ArrayList<Booking>());
        customerService.updateCustomerDetails(customer);
        System.out.println("All bookings delete for customer :" + customer.getUsername());
        return new ResponseEntity<String>("All bookings delete for customer :" +  customer.getUsername() ,HttpStatus.OK);
    }
}
