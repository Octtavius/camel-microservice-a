package com.beans;

import org.springframework.stereotype.Component;

import java.util.Random;

// Generate random number between 1-3.

@Component
public class MessageGenerator {
    private Random random = new java.util.Random();

    public String getMessage() {

//        int tmp = 3;
        int tmp = random.nextInt(3) + 1;
        return "My message for Active MQ "+ " " + System.currentTimeMillis() + " " + tmp;
    }
}
