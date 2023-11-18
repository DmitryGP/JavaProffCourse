package ru.dgp;

import java.util.ArrayDeque;
import java.util.Deque;

@SuppressWarnings({"java:S1186", "java:S1135", "java:S1172"}) // при выполнении ДЗ эту аннотацию надо удалить
public class CustomerReverseOrder {

    private final Deque<Customer>  stack = new ArrayDeque<>();

    public void add(Customer customer) {
        stack.addFirst(customer);
    }

    public Customer take() {
        return stack.pollFirst();
    }
}
