package com.xebia.reactive_programming.config.router;

import com.xebia.reactive_programming.handler.MonoFluxHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> route(MonoFluxHandler monoFluxHandler) {
        return RouterFunctions
                .route()
                .path("/functional", b1 -> b1.nest(accept(MediaType.APPLICATION_JSON), b2 -> b2
                        .GET("/flux", monoFluxHandler::flux)
                        .GET("/mono", monoFluxHandler::mono)))
                .GET("/ping", accept(MediaType.APPLICATION_JSON)
                        , request -> ServerResponse.ok().bodyValue("{\n\"ping\":\"pong\"\n}").log())
                .build();
    }

}
