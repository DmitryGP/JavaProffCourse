package ru.dgp;

import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IocDemo {

    private static final Logger logger = LoggerFactory.getLogger(IocDemo.class);
    private static final String LOG_CALCULATED_VALUE_TEMPLATE = "Calculated value: {}";

    public static void main(String[] args)
            throws NoInterfacesImplemented, NoSuchMethodException, InvocationTargetException, InstantiationException,
                    IllegalAccessException {
        Ioc<CalculatorImpl> ioc = new Ioc<>();
        Calculator calculator = ioc.createInstance(CalculatorImpl.class);

        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(10));
        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(5, 11));
        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(1, 2, 3));
        logger.info(LOG_CALCULATED_VALUE_TEMPLATE, calculator.calculate(1, 1));
    }
}
