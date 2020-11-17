package com.xebia.reactive_programming.repository;

import com.xebia.reactive_programming.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CustomerRepositoryTests {


    @Autowired
    private CustomerRepository customers;
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

    @Test
    public void executesFindAll() throws IOException {

        Customer dave = new Customer(null, "Dave", "Matthews");
        Customer carter = new Customer(null, "Carter", "Beauford");

        insertCustomers(dave, carter);

        customers.findAll() //
                .as(StepVerifier::create) //
                .assertNext(dave::equals) //
                .assertNext(carter::equals) //
                .verifyComplete();
    }

    @Test
    public void executesAnnotatedQuery() throws IOException {

        Customer dave = new Customer(null, "Dave", "Matthews");
        Customer carter = new Customer(null, "Carter", "Beauford");

        insertCustomers(dave, carter);

        customers.findByLastname("Matthews") //
                .as(StepVerifier::create) //
                .assertNext(dave::equals) //
                .verifyComplete();
    }

    private void insertCustomers(Customer... customers) {

        this.customers.saveAll(Arrays.asList(customers))//
                .as(StepVerifier::create) //
                .expectNextCount(2) //
                .verifyComplete();
    }

}