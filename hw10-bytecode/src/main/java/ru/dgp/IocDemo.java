package ru.dgp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IocDemo {

    private static final Logger logger = LoggerFactory.getLogger(IocDemo.class);
    private static final String LOG_CALCULATED_VALUE_TEMPLATE = "Calculated value: {}";

    public static void main(String[] args) throws InitializationException {
        Calculator calculator = Ioc.createCalculator();

        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(10));
        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(5, 11));
        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(1, 2, 3));
        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(1, 1));
    }
}
