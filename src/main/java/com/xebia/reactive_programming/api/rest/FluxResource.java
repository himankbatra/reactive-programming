package com.xebia.reactive_programming.api.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxResource {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {

        return Flux.just(1, 2, 3, 4)
                .log();

    }

    @GetMapping(value = "/flux-stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> returnFluxStream() {

        return Flux.interval(Duration.ofSeconds(1))
                .log();

    }


}
