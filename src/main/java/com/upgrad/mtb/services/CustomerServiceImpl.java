package com.upgrad.mtb.services;

import com.upgrad.mtb.beans.City;
import com.upgrad.mtb.beans.Customer;
import com.upgrad.mtb.beans.UserType;
import com.upgrad.mtb.controllers.MovieController;
import com.upgrad.mtb.daos.CustomerDAO;
import com.upgrad.mtb.daos.UserTypeDAO;
import com.upgrad.mtb.dto.CustomerDTO;
import com.upgrad.mtb.exceptions.CustomerDetailsNotFoundException;
import com.upgrad.mtb.exceptions.CustomerUserNameExistsException;
import com.upgrad.mtb.exceptions.UserTypeDetailsNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


@Service(value = "customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    @Qualifier("customerDAO")
    CustomerDAO customerDAO;
    @Autowired
    UserTypeService userTypeService;

    private Map<String, Customer> refreshTokenUserMap;

    private List<String> tokenStore;

    private Map<String, String> refreshTokenAccessTokenMap;

    private Map<String, Customer> accessTokenUserMap;

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @PostConstruct
    public void init() {
        refreshTokenUserMap = new HashMap<>();
        tokenStore = new ArrayList<>();
        refreshTokenAccessTokenMap = new HashMap<>();
        accessTokenUserMap = new HashMap<>();
    }

    public void updateAccessTokenUserMap(String accessTokenPrev, String accessTokenNew, Customer customerUser) {
        if (accessTokenUserMap.containsKey(accessTokenPrev)) {
            Customer user = accessTokenUserMap.get(accessTokenPrev);
            accessTokenUserMap.remove(accessTokenPrev);
            accessTokenUserMap.put(accessTokenNew, user);
        } else {
            accessTokenUserMap.put(accessTokenNew, customerUser);
        }
    }

    public Customer getUserFromAccessToken(String accessToken) {
        return accessTokenUserMap.get(accessToken);
    }

    public void removeUserFromAccessTokenMap(String accessToken) {
        if (accessTokenUserMap.containsKey(accessToken)) {
            accessTokenUserMap.remove(accessToken);
        }
    }

    public boolean isTokenPresent(String token) {
        return tokenStore.contains(token);
    }

    public void removeTokenIfPresent(String token) {
        if (tokenStore.contains(token)) {
            tokenStore.remove(token);
        }
    }

    public void updateRefreshTokenAccessTokenMap(String refreshToken, String accessToken) {
        this.refreshTokenAccessTokenMap.put(refreshToken, accessToken);
    }

    public void removeRefreshTokenAccessTokenMap(String refreshToken) {
        if (this.refreshTokenAccessTokenMap.containsKey(refreshToken)) {
            this.refreshTokenAccessTokenMap.remove(refreshToken);
        }
    }

    public String getCurrentAccessTokenFromRefreshToken(String refreshToken) {
        return this.refreshTokenAccessTokenMap.get(refreshToken);
    }

    public void addToken(String token) {
        tokenStore.add(token);
    }

    public void removeRefreshToken(String refreshToken) {
        if (refreshTokenUserMap.containsKey(refreshToken)) {
            refreshTokenUserMap.remove(refreshToken);
        }
    }

    public Customer getUserfromRefreshToken(String refreshToken) {
        return refreshTokenUserMap.get(refreshToken);
    }

    public String getRefreshTokenForUser(String username) {

        Customer customerUser = null;
        List<Customer> customers = getAllCustomerDetails();
        for (Customer user : customers) {
            if (user.getUsername().equals(username)) {
                customerUser = user;
                break;
            }
        }

        if (customerUser == null) { //User is not present
            return null;
        }
        for (Map.Entry<String, Customer> entry : refreshTokenUserMap.entrySet()) {
            if (entry.getValue().equals(customerUser)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void addRefreshToken(String refreshToken, Customer customer) {
        this.refreshTokenUserMap.put(refreshToken, customer);
    }


    @Override
    public Customer acceptCustomerDetails(Customer customer) throws CustomerUserNameExistsException, UserTypeDetailsNotFoundException {
        logger.debug("Entered acceptCustomerDetails", customer);
        if(customerDAO.findByUsername(customer.getUsername()).isEmpty()){
           return  customerDAO.save(customer);
        }else{
            throw new CustomerUserNameExistsException("This username already exists please choose another : " + customer.getUsername());
        }
    }

    @Override
    public Customer getCustomerDetails(int id) throws CustomerDetailsNotFoundException {
        logger.debug("get customer details" , id);
        System.out.println("Get customer details ");
        Customer customer = customerDAO.findById(id).orElseThrow(
                ()->  new CustomerDetailsNotFoundException("Customer not found for id" + id));
        return customer;
    }

    @Override
    public UserDetails loadCustomerDetails(String username) throws CustomerDetailsNotFoundException {
        Customer customer = customerDAO.findByUsername(username).orElseThrow(
                ()->  new CustomerDetailsNotFoundException("Customer not found for " + username));

        return  new User(customer.getUsername(), customer.getPassword() , new ArrayList<>());

    }

    public Customer getCustomerDetailsByUsername(String username) throws CustomerDetailsNotFoundException {
        Customer customer = customerDAO.findByUsername(username).orElseThrow(
                ()->  new CustomerDetailsNotFoundException("Customer not found for username" + username));
        return customer;
    }

    @Override
    public Customer updateCustomerDetails(int initialCustomerId, Customer customer) throws CustomerDetailsNotFoundException, UserTypeDetailsNotFoundException {
        Customer initialCustomer = getCustomerDetails(initialCustomerId);
        System.out.println("Initial customer details : " + initialCustomer.toString());
        initialCustomer.setFirstName(customer.getFirstName());
        initialCustomer.setLastName(customer.getLastName());
        initialCustomer.setPassword(customer.getPassword());
        initialCustomer.setDateOfBirth(customer.getDateOfBirth());
        initialCustomer.setUserType(customer.getUserType());
        initialCustomer.setPhoneNumbers(customer.getPhoneNumbers());
        initialCustomer.setBookings(customer.getBookings());
        customerDAO.save(initialCustomer);
        System.out.println("New city details :" + getCustomerDetails(initialCustomerId).toString());
        return initialCustomer;

    }

    public Customer updateCustomerDetails(Customer customer){
        return customerDAO.save(customer);
    }

    @Override
    public boolean deleteCustomer(int id) throws CustomerDetailsNotFoundException {
        System.out.println("Deleting customer details : customerServiceImpl ");
        Customer customer = getCustomerDetails(id);
        System.out.println(customer.toString());
        customerDAO.delete(customer);
        return true;
    }

    @Override
    public List<Customer> getAllCustomerDetails() {
        logger.debug("Entered getAllCustomerDetails : customerServiceImpl");
        return customerDAO.findAll();
    }
}
