package ru.dgp;

public class CalculatorImpl implements Calculator {
    @Override
    public int calculate(int v1) {
        return v1 * 10;
    }

    @Override
    @Log
    public int calculate(int v1, int v2) {
        return v1 + v2;
    }

    @Override
    public int calculate(int v1, int v2, int v3) {
        return v1 + v2 * v3;
    }
}
