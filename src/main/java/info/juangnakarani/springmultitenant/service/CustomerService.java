package info.juangnakarani.springmultitenant.service;

import info.juangnakarani.springmultitenant.entity.Customer;
import info.juangnakarani.springmultitenant.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer save(Customer customer);

    Customer update(Customer customer, Long id);

    void delete(Long id);
}
