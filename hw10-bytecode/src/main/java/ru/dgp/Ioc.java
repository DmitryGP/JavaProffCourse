package ru.dgp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static Calculator createCalculator() throws InitializationException {
        InvocationHandler handler = new LogInvocationHandler(new CalculatorImpl());

        return (Calculator)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {Calculator.class}, handler);
    }

    static class LogInvocationHandler implements InvocationHandler {

        private final Calculator calculator;
        private HashMap<Method, Method> methodsToLog = new HashMap<>();

        LogInvocationHandler(Calculator calculator) throws InitializationException {

            this.calculator = calculator;
            initMethodsToLogMap();
        }

        private void initMethodsToLogMap() throws InitializationException {
            Class<? extends Calculator> clazz = calculator.getClass();
            Class<Calculator> interfacce = Calculator.class;

            try {
                Method[] calMethods = interfacce.getMethods();

                for (Method m : calMethods) {
                    Method classMethod = clazz.getMethod(m.getName(), m.getParameterTypes());

                    if (classMethod.isAnnotationPresent(Log.class)) {
                        methodsToLog.put(m, classMethod);
                    }
                }
            } catch (NoSuchMethodException exc) {
                throw new InitializationException("Exception while initializing invocation handler", exc);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Method executedMethod = methodsToLog.get(method);

            if (executedMethod != null) {
                String name = method.getName();
                List<String> argValues =
                        Arrays.stream(args).map(Object::toString).toList();

                logger.atInfo()
                        .setMessage("executed method {}, param(s): {}")
                        .addArgument(name)
                        .addArgument(String.join(", ", argValues))
                        .log();
            }

            return method.invoke(calculator, args);
        }
    }
}
