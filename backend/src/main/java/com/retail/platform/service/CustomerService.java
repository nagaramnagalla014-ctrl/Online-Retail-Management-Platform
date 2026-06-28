package com.retail.platform.service;

import com.retail.platform.model.Customer;
import com.retail.platform.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;

    public Customer register(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail()))
            throw new RuntimeException("Email already registered: " + customer.getEmail());
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> login(String email, String password) {
        return customerRepository.findByEmailAndPassword(email, password);
    }

    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }

    public Customer update(Long id, Customer updated) {
        Customer existing = getById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setState(updated.getState());
        existing.setPincode(updated.getPincode());
        return customerRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public long count() {
        return customerRepository.countByIsActiveTrue();
    }
}
