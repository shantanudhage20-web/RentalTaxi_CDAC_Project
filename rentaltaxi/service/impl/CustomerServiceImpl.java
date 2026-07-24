package com.rentaltaxi.service.impl;

import com.rentaltaxi.dto.request.CustomerRegistrationRequest;
import com.rentaltaxi.dto.request.CustomerUpdateRequest;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.exception.BadRequestException;
import com.rentaltaxi.exception.ResourceNotFoundException;
import com.rentaltaxi.repository.CustomerRepository;
import com.rentaltaxi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Customer registerCustomer(CustomerRegistrationRequest request) {
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        Customer customer = Customer.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer updateCustomer(Integer customerId, CustomerUpdateRequest request) {
        Customer customer = getCustomerById(customerId);
        if (request.getFullName() != null) customer.setFullName(request.getFullName());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(customer.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already taken");
            }
            customer.setEmail(request.getEmail());
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with username: " + username));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCustomer(Integer id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}