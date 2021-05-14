package com.tt.shopping.impl.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SequenceGenerator {

    public String generate() {
        Random rand = new Random();
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            int n = rand.nextInt(10) + 0;
            sequence.append(Integer.toString(n));
        }

        return sequence.toString();
    }
}
