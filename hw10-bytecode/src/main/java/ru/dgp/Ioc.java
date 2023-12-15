package ru.dgp;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ioc<T> {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    public T createInstance(Class<T> classToCreate)
            throws NoInterfacesImplemented, NoSuchMethodException, InvocationTargetException, InstantiationException,
                    IllegalAccessException {

        Constructor<T> constructor = classToCreate.getConstructor();

        T instance = constructor.newInstance();

        InvocationHandler handler = new LogInvocationHandler<>(instance);

        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), classToCreate.getInterfaces(), handler);
    }

    static class LogInvocationHandler<T> implements InvocationHandler {

        private final T instance;

        private final AnnotatedMethodsContainer annotatedMethodsContainer;

        LogInvocationHandler(T instance) throws NoInterfacesImplemented, NoSuchMethodException {

            this.instance = instance;
            annotatedMethodsContainer = new AnnotatedMethodsContainer(instance.getClass(), Log.class);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Method executedMethod = annotatedMethodsContainer.getAnnotatedMethod(method);

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

            return method.invoke(instance, args);
        }
    }
}
