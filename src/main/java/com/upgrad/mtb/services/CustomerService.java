package com.upgrad.mtb.services;

import com.upgrad.mtb.entity.Customer;
import com.upgrad.mtb.exceptions.CustomerDetailsNotFoundException;
import com.upgrad.mtb.exceptions.CustomerUserNameExistsException;
import com.upgrad.mtb.exceptions.UserTypeDetailsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {
     public Customer acceptCustomerDetails(Customer customer) throws CustomerUserNameExistsException, UserTypeDetailsNotFoundException;
     public Customer getCustomerDetails(int id) throws CustomerDetailsNotFoundException;
     public Customer getCustomerDetailsByUsername(String username) throws CustomerDetailsNotFoundException;
     public UserDetails loadCustomerDetails(String username) throws CustomerDetailsNotFoundException;
     public Customer updateCustomerDetails(int initialCustomerId, Customer customer) throws CustomerDetailsNotFoundException, UserTypeDetailsNotFoundException;
}
