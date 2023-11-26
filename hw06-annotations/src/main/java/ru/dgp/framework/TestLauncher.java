package ru.dgp.framework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dgp.annotations.After;
import ru.dgp.annotations.Before;
import ru.dgp.annotations.Test;

public final class TestLauncher {

    private static final Logger logger = LoggerFactory.getLogger(TestLauncher.class);

    private TestLauncher() {}

    public static void launch(Class<?> testClass) {

        Method[] beforeMethods = AnnotationHelper.getAnnotatedMethods(testClass, Before.class);
        Method[] afterMethods = AnnotationHelper.getAnnotatedMethods(testClass, After.class);
        Method[] tests = AnnotationHelper.getAnnotatedMethods(testClass, Test.class);

        List<TestState> testStates = new ArrayList<>();

        for (int i = 0; i < tests.length; i++) {
            TestState state = launchTest(testClass, tests[i], beforeMethods, afterMethods);

            testStates.add(state);
        }

        showStatistics(testClass, testStates);
    }

    private static void showStatistics(Class<?> testClass, List<TestState> testStates) {
        logger.info("Statistics for test launching for class {}: ", testClass.getName());

        testStates.stream().forEach(s -> logger.info(s.toString()));
    }

    private static <T> TestState launchTest(
            Class<T> testClass, Method test, Method[] beforeMethods, Method[] afterMethods) {

        try {
            Constructor<T> cnstrtr = testClass.getDeclaredConstructor();
            Object o = cnstrtr.newInstance();

            for (int i = 0; i < beforeMethods.length; i++) {
                beforeMethods[i].invoke(o);
            }

            test.invoke(o);

            for (int i = 0; i < afterMethods.length; i++) {
                afterMethods[i].invoke(o);
            }

            return new TestState(test.getName(), State.OK, null);
        } catch (Exception exc) {
            return new TestState(test.getName(), State.FAILED, exc);
        }
    }

    private enum State {
        OK,
        FAILED
    }

    private static class TestState {

        private String name;
        private State state;
        private Exception exception;

        public TestState(String name, State state, Exception exc) {
            this.name = name;
            this.state = state;
            this.exception = exc;
        }

        public String getName() {
            return name;
        }

        public State getState() {
            return state;
        }

        public Exception getException() {
            return exception;
        }

        @Override
        public String toString() {
            return this.name + "\t" + this.state + "\t" + this.exception.toString();
        }
    }
}
