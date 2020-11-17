package com.xebia.reactive_programming.service;

import com.xebia.reactive_programming.domain.Customer;
import com.xebia.reactive_programming.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CustomerServiceTests {

    @Autowired
    private CustomerService service;
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private DatabaseClient database;

    @BeforeEach
    public void setUp() {

        Hooks.onOperatorDebug();

        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS customer;",
                "CREATE TABLE customer ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);");

        statements.forEach(it -> database.execute(it) //
                .fetch() //
                .rowsUpdated() //
                .as(StepVerifier::create) //
                .expectNextCount(1) //
                .verifyComplete());
    }

    @Test // #500
    public void exceptionTriggersRollback() {

        service.save(new Customer(null, "Dave", "Matthews")) //
                .as(StepVerifier::create) //
                .expectError() // Error because of the exception triggered within the service
                .verify();

        // No data inserted due to rollback
        repository.findByLastname("Matthews") //
                .as(StepVerifier::create) //
                .verifyComplete();
    }

    @Test // #500
    public void insertsDataTransactionally() {

        service.save(new Customer(null, "Carter", "Beauford")) //
                .as(StepVerifier::create) //
                .expectNextMatches(Customer::hasId) //
                .verifyComplete();

        // Data inserted due to commit
        repository.findByLastname("Beauford") //
                .as(StepVerifier::create) //
                .expectNextMatches(Customer::hasId) //
                .verifyComplete();
    }


}