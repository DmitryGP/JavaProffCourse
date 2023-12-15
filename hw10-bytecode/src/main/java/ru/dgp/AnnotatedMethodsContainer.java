package ru.dgp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AnnotatedMethodsContainer {

    private final HashMap<Method, Method> annotatedMethodsMap = new HashMap<>();
    private boolean hasAnnotatedMethods;

    public AnnotatedMethodsContainer(Class<?> clazz, Class<? extends Annotation> annotation)
            throws NoInterfacesImplemented, NoSuchMethodException {
        Class<?>[] interfaces = clazz.getInterfaces();

        hasAnnotatedMethods = false;

        if (interfaces.length == 0) {
            throw new NoInterfacesImplemented();
        }

        for (Class<?> infc : interfaces) {
            Method[] infcMethods = infc.getMethods();

            for (Method infcMethod : infcMethods) {
                Method classMethod = clazz.getMethod(infcMethod.getName(), infcMethod.getParameterTypes());

                if (classMethod.isAnnotationPresent(annotation)) {
                    annotatedMethodsMap.put(infcMethod, classMethod);
                    hasAnnotatedMethods = true;
                }
            }
        }
    }

    public Method getAnnotatedMethod(Method interfaceMethod) {
        if (!hasAnnotatedMethods) {
            return null;
        }

        return annotatedMethodsMap.get(interfaceMethod);
    }
}
