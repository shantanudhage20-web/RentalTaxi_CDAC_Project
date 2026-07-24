package com.rentaltaxi.service;

import com.rentaltaxi.dto.request.CustomerRegistrationRequest;
import com.rentaltaxi.dto.request.CustomerUpdateRequest;
import com.rentaltaxi.dto.response.CustomerResponse;
import com.rentaltaxi.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer registerCustomer(CustomerRegistrationRequest request);
    Customer updateCustomer(Integer customerId, CustomerUpdateRequest request);
    Customer getCustomerById(Integer id);
    Customer getCustomerByUsername(String username);
    List<Customer> getAllCustomers();
    void deleteCustomer(Integer id);
}
