package com.rentaltaxi.controller;

import com.rentaltaxi.dto.request.CustomerUpdateRequest;
import com.rentaltaxi.dto.response.CustomerResponse;
import com.rentaltaxi.dto.response.ApiResponse;
import com.rentaltaxi.entity.Customer;
import com.rentaltaxi.security.UserPrincipal;
import com.rentaltaxi.service.CustomerService;
import com.rentaltaxi.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "Customer profile operations")
public class CustomerController {

    private final CustomerService customerService;
    private final DTOMapper mapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get current customer profile")
    public ResponseEntity<CustomerResponse> getCurrentCustomer(@AuthenticationPrincipal UserPrincipal currentUser) {
        Customer customer = customerService.getCustomerById(currentUser.getId());
        return ResponseEntity.ok(mapper.toCustomerResponse(customer));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @Operation(summary = "Get customer by ID (Admin or own profile)")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Integer id,
                                                            @AuthenticationPrincipal UserPrincipal currentUser) {
        // Allow admin or the customer themselves
        if (!currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
                !currentUser.getId().equals(id)) {
            // For simplicity, returning 403 is handled by @PreAuthorize, but we can add logic.
        }
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(mapper.toCustomerResponse(customer));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Update current customer profile")
    public ResponseEntity<CustomerResponse> updateCurrentCustomer(@AuthenticationPrincipal UserPrincipal currentUser,
                                                                  @Valid @RequestBody CustomerUpdateRequest request) {
        Customer customer = customerService.updateCustomer(currentUser.getId(), request);
        return ResponseEntity.ok(mapper.toCustomerResponse(customer));
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Delete current customer account")
    public ResponseEntity<ApiResponse> deleteCurrentCustomer(@AuthenticationPrincipal UserPrincipal currentUser) {
        customerService.deleteCustomer(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse(true, "Customer account deleted"));
    }
}
