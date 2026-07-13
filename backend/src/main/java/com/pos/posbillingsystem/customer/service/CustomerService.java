package com.pos.posbillingsystem.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pos.posbillingsystem.customer.dto.CustomerRequest;
import com.pos.posbillingsystem.customer.dto.CustomerResponse;
import com.pos.posbillingsystem.customer.entity.Customer;
import com.pos.posbillingsystem.customer.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create Customer
    public CustomerResponse createCustomer(CustomerRequest request) {

        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        Customer customer = new Customer();

        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setLoyaltyPoints(request.getLoyaltyPoints());
        customer.setStatus(request.getStatus());

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    // Get All Customers
    public List<CustomerResponse> getAllCustomers() {

        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get Customer By Id
    public CustomerResponse getCustomerById(Integer id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return mapToResponse(customer);
    }

    // Update Customer
    public CustomerResponse updateCustomer(Integer id, CustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getPhone().equals(request.getPhone())
                && customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        customer.setCustomerName(request.getCustomerName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setLoyaltyPoints(request.getLoyaltyPoints());
        customer.setStatus(request.getStatus());

        Customer updatedCustomer = customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }

    // Delete Customer
    public void deleteCustomer(Integer id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerRepository.delete(customer);
    }

    // Entity -> Response DTO
    private CustomerResponse mapToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customer.getCustomerId());
        response.setCustomerName(customer.getCustomerName());
        response.setPhone(customer.getPhone());
        response.setEmail(customer.getEmail());
        response.setAddress(customer.getAddress());
        response.setLoyaltyPoints(customer.getLoyaltyPoints());
        response.setStatus(customer.getStatus());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }
}