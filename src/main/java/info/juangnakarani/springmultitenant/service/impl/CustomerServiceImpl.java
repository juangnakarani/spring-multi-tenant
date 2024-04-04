package info.juangnakarani.springmultitenant.service.impl;

import info.juangnakarani.springmultitenant.entity.Customer;
import info.juangnakarani.springmultitenant.repository.CustomerRepository;
import info.juangnakarani.springmultitenant.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer, Long id) {
        Customer c = customerRepository.findById(id).get();
        c.setName(customer.getName());
        return customerRepository.save(c);
    }

    @Override
    public void delete(Long id) {

    }
}
