package ru.dgp.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationHelper {

    private AnnotationHelper() {}

    public static List<Method> getAnnotatedMethods(Class<?> aClass, Class<? extends Annotation> annotation) {
        Method[] methods = aClass.getDeclaredMethods();

        List<Method> beforeMethods = new ArrayList<>();

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(annotation)) {
                beforeMethods.add(methods[i]);
            }
        }

        return beforeMethods;
    }
}
