package info.juangnakarani.springmultitenant.service;

import info.juangnakarani.springmultitenant.entity.Customer;
import info.juangnakarani.springmultitenant.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
}
