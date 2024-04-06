package info.juangnakarani.springmultitenant.controller;

import info.juangnakarani.springmultitenant.entity.Customer;
import info.juangnakarani.springmultitenant.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/list")
    public ResponseEntity<List<Customer>> getCustomer() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping("/create")
    public Customer save(@RequestBody Customer customer){
        return customerService.save(customer);
    }

    @PutMapping("/update/{id}")
    public Customer update(@RequestBody Customer customer, @PathVariable("id") Long customerId){
        return customerService.update(customer, customerId);
    }

    @GetMapping("/get/{id}")
    public Customer get(@PathVariable("id") Long customerId){
        return customerService.get(customerId);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        customerService.delete(id);
        return "Deleted";
    }

}
