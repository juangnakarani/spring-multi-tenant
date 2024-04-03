package info.juangnakarani.springmultitenant.repository;

import info.juangnakarani.springmultitenant.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
