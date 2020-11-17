package com.xebia.reactive_programming.service;

import com.xebia.reactive_programming.domain.Customer;
import com.xebia.reactive_programming.repository.CustomerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final @NonNull CustomerRepository repository;

    /**
     * Saves the given {@link com.xebia.reactive_programming.domain.Customer} unless its firstname is "Dave".
     *
     * @param customer must not be {@literal null}.
     * @return
     */
    @Transactional
    public Mono<Customer> save(Customer customer) {

        return repository.save(customer).map(c -> {
            if (c.getFirstname().equals("Dave")) {
                throw new IllegalStateException();
            } else {
                return c;
            }
        });
    }

}
