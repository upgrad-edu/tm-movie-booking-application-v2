package com.upgrad.mtb.services;

import com.upgrad.mtb.entity.Customer;
import com.upgrad.mtb.exceptions.CustomerDetailsNotFoundException;
import com.upgrad.mtb.exceptions.CustomerUserNameExistsException;
import com.upgrad.mtb.exceptions.UserTypeDetailsNotFoundException;

public interface CustomerService {
     public Customer acceptCustomerDetails(Customer customer) throws CustomerUserNameExistsException, UserTypeDetailsNotFoundException;
     public Customer getCustomerDetails(int id) throws CustomerDetailsNotFoundException;
     public Customer getCustomerDetailsByUsername(String username) throws CustomerDetailsNotFoundException;
     // TODO in security session
     // public UserDetails loadCustomerDetails(String username) throws CustomerDetailsNotFoundException;
     public Customer updateCustomerDetails(int initialCustomerId, Customer customer) throws CustomerDetailsNotFoundException, UserTypeDetailsNotFoundException;
}
