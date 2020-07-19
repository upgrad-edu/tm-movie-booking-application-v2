package com.upgrad.mtb.controllers;


import com.upgrad.mtb.beans.Customer;
import com.upgrad.mtb.dto.CustomerDTO;
import com.upgrad.mtb.dto.LoginDTO;
import com.upgrad.mtb.dto.RefreshTokenRequest;
import com.upgrad.mtb.dto.ResetPasswordDTO;
import com.upgrad.mtb.exceptions.*;
import com.upgrad.mtb.security.jwt.JwtTokenProvider;
import com.upgrad.mtb.services.CustomerServiceImpl;
import com.upgrad.mtb.utils.DTOEntityConverter;
import com.upgrad.mtb.utils.EntityDTOConverter;
import com.upgrad.mtb.validator.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    CustomerServiceImpl customerService;
    @Autowired
    CustomerValidator customerValidator;
    @Autowired
    EntityDTOConverter entityDTOConverter;
    @Autowired
    DTOEntityConverter dtoEntityConverter;


    @RequestMapping(method = RequestMethod.POST, value = "/customers")
    @ResponseBody
    public ResponseEntity signUp(@RequestBody CustomerDTO customerDTO) throws CustomException, APIException, CustomerUserNameExistsException {
        System.out.println("entered sign up");
        customerValidator.validateCustomer(customerDTO);
        try{
            Customer customer = customerService.getCustomerDetailsByUsername(customerDTO.getUsername());
            if(customer != null)
                throw new CustomerUserNameExistsException("Customer username already exists : " + customerDTO.getUsername());
        }catch (CustomerDetailsNotFoundException ex){
            System.out.println("Customer does not exist for the given details");
        }
        try {
            Map<String, String> model = new HashMap<>();
            String username = customerDTO.getUsername();
            String password = customerDTO.getPassword();
            if(StringUtils.isEmpty(username) ||  StringUtils.isEmpty(password)){
                model.put("Error", "Username is invalid/ Password is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(model);
            }
            String refreshToken = jwtTokenProvider.createRefreshToken(username);
            String token = jwtTokenProvider.createToken(username);
            Customer newCustomer = dtoEntityConverter.convertToCustomerEntity(customerDTO);
            Customer savedCustomer = customerService.acceptCustomerDetails(newCustomer);
            CustomerDTO savedCustomerDTO = entityDTOConverter.convertToCustomerDTO(savedCustomer);
            savedCustomerDTO.setJwtToken(token);
            savedCustomerDTO.setRefreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO);
        } catch (Exception e) {
            throw new CustomException("Username " + customerDTO.getUsername() + " already registered", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/access-tokens")
    @ResponseBody
    public ResponseEntity signIn(@RequestBody LoginDTO loginDTO) throws APIException, CustomerDetailsNotFoundException,BadCredentialsException, CustomException {
        System.out.println("Print statement here _____________________________");
        try {
            customerValidator.validateuserLogin(loginDTO);
            Map<String, String> model = new HashMap<>();
            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();
            if(StringUtils.isEmpty(username) ||  StringUtils.isEmpty(password)){
                model.put("Error", "Username is invalid/ Password is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(model);
            }
            Customer savedCustomer = customerService.getCustomerDetailsByUsername(username);
            if(!savedCustomer.getPassword().equals(password)){
                throw new BadCredentialsException("Invalid username/password");
            }
            String refreshToken = jwtTokenProvider.createRefreshToken(username);
            String token = jwtTokenProvider.createToken(username);
            CustomerDTO savedCustomerDTO = entityDTOConverter.convertToCustomerDTO(savedCustomer);
            savedCustomerDTO.setJwtToken(token);
            savedCustomerDTO.setRefreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO);
        } catch (Exception e) {
            throw new CustomException("Username " + loginDTO.getUsername() + "not registered/password incorrect", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }



    @RequestMapping(method = RequestMethod.PUT, value = "/resetPassword")
    @ResponseBody
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws CustomException {
        try {
            customerValidator.validateResetPassword(resetPasswordDTO);
            Map<String, String> model = new HashMap<>();
            String username = resetPasswordDTO.getUsername();
            String oldPassword = resetPasswordDTO.getOldPassword();
            if(StringUtils.isEmpty(username) ||  StringUtils.isEmpty(oldPassword)){
                model.put("Error", "Username is invalid/ Password is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(model);
            }
            Customer savedCustomer = customerService.getCustomerDetailsByUsername(username);
            if(!savedCustomer.getPassword().equals(oldPassword)){
                throw new BadCredentialsException("Invalid username/password");
            }
            String refreshToken = jwtTokenProvider.createRefreshToken(username);
            String token = jwtTokenProvider.createToken(username);
            savedCustomer.setPassword(resetPasswordDTO.getNewPassword());
            savedCustomer = customerService.updateCustomerDetails(savedCustomer.getId(),savedCustomer);
            CustomerDTO savedCustomerDTO = entityDTOConverter.convertToCustomerDTO(savedCustomer);
            savedCustomerDTO.setJwtToken(token);
            savedCustomerDTO.setRefreshToken(refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO);
        } catch (Exception e) {
            throw new CustomException("Username :" + resetPasswordDTO.getUsername() + "Invalid UserName/Password", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
