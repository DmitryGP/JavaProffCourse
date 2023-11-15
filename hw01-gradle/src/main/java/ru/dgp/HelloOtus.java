package ru.dgp;

import com.google.common.base.Joiner;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String[] args) {

        String[] parts = new String[] {"Apple", "Orange", "Banana"};

        String result = Joiner.on(", ").join(parts);

        System.out.println(result);
    }
}
