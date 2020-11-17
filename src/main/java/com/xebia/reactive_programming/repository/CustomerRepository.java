package com.xebia.reactive_programming.repository;

import com.xebia.reactive_programming.domain.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("select id, firstname, lastname from customer c where c.lastname = :lastname")
    Flux<Customer> findByLastname(String lastname);
}
