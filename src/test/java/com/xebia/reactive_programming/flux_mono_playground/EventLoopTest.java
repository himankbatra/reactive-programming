package com.xebia.reactive_programming.flux_mono_playground;


import org.junit.jupiter.api.Test;

public class EventLoopTest {

    @Test
    public void noOfProcessors(){

        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
