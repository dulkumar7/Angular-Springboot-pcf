package com.customer.demo.restControllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.customer.demo.model.Customer;
import com.customer.demo.repo.CustomerRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@CrossOrigin
@RestController
public class CustomerController {

	@Autowired
	CustomerRepository repository;

	@HystrixCommand(fallbackMethod = "fallbackcustomerDetails", commandKey = "customers", groupKey = "customers")
	@GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		System.out.println("Get all Customers...");

		List<Customer> customers = new ArrayList<>();
		repository.findAll().forEach(customers::add);

		return customers;
	}

	// fallback for getCustomers
	public List<Customer> fallbackcustomerDetails() {

		List<Customer> customers = new ArrayList<>();

		customers.add(new Customer("fallback", 1));
		customers.add(new Customer("fallback", 2));

		return customers;

	}

	@PostMapping(value = "/customers/create")
	public Customer postCustomer(@RequestBody Customer customer) {

		Customer _customer = repository.save(new Customer(customer.getName(), customer.getAge()));
		return _customer;
	}

	@HystrixCommand(fallbackMethod = "deletedCustomer")
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		System.out.println("Delete Customer with ID = " + id + "...");

		repository.deleteById(id);

		return new ResponseEntity<>("Customer has been deleted!", HttpStatus.OK);
	}

	// fallback for deletecustomer
	public ResponseEntity<String> deletedCustomer() {

		return new ResponseEntity<>("customer is not available", null);
	}

	@HystrixCommand(fallbackMethod = "deletedAllCustomers")
	@DeleteMapping("/customers/delete")
	public ResponseEntity<String> deleteAllCustomers() {
		System.out.println("Delete All Customers...");

		repository.deleteAll();

		return new ResponseEntity<>("All customers have been deleted!", HttpStatus.OK);
	}

	// fallback for deleteAllCustomers
	public ResponseEntity<String> deletedAllCustomers() {

		return new ResponseEntity<>("There is no more customers", null);
	}

	@GetMapping(value = "customers/age/{age}")
	@HystrixCommand(fallbackMethod = "getCustomerByAge")
	public List<Customer> findByAge(@PathVariable int age) {

		List<Customer> customers = repository.findByAge(age);
		return customers;
	}

	// fallback

	public List<Customer> getCustomerByAge(@PathVariable int age) {

		List<Customer> customers = new ArrayList<>();

		return customers;

	}

	}