package com.customer.demo.repo;

import java.util.List;
 
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.customer.demo.model.Customer;
 
@Repository 
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	List<Customer> findByAge(int age);
}

