package com.xebia.reactive_programming.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Customer {

    @Id
    private Integer id;
    private String firstname, lastname;

    public boolean hasId() {
        return id != null;
    }
}