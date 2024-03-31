package com.example.practice.spring6reactive.repositories;

import com.example.practice.spring6reactive.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
