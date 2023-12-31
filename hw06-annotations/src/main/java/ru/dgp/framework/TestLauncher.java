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

    public static void launch(String testClassName) throws ClassNotFoundException {

        Class<?> testClass = Class.forName(testClassName);

        List<Method> beforeMethods = AnnotationHelper.getAnnotatedMethods(testClass, Before.class);
        List<Method> afterMethods = AnnotationHelper.getAnnotatedMethods(testClass, After.class);
        List<Method> tests = AnnotationHelper.getAnnotatedMethods(testClass, Test.class);

        List<TestState> testStates = new ArrayList<>();

        for (int i = 0; i < tests.size(); i++) {
            TestState state = launchTest(testClass, tests.get(i), beforeMethods, afterMethods);

            testStates.add(state);
        }

        showStatistics(testClass, testStates);
    }

    private static void showStatistics(Class<?> testClass, List<TestState> testStates) {
        logger.info("Statistics for test launching for class {}: ", testClass.getName());

        testStates.stream().forEach(s -> logger.info(s.toString()));

        long okCount = testStates.stream().filter(s -> s.state == State.OK).count();
        long failedCount =
                testStates.stream().filter(s -> s.state == State.FAILED).count();

        logger.info("TOTAL : {} TESTS, SUCCEED: {}, FAILED: {}", testStates.size(), okCount, failedCount);
    }

    private static <T> TestState launchTest(
            Class<T> testClass, Method test, List<Method> beforeMethods, List<Method> afterMethods) {

        try {
            Constructor<T> cnstrtr = testClass.getDeclaredConstructor();
            Object o = cnstrtr.newInstance();

            for (int i = 0; i < beforeMethods.size(); i++) {
                beforeMethods.get(i).invoke(o);
            }

            test.invoke(o);

            for (int i = 0; i < afterMethods.size(); i++) {
                afterMethods.get(i).invoke(o);
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

        private final String name;
        private final State state;
        private final Exception exception;

        private final String toString;

        public TestState(String name, State state, Exception exc) {
            this.name = name;
            this.state = state;
            this.exception = exc;

            toString = "\t" + this.name + "\t" + this.state + "\t"
                    + (this.exception != null ? this.exception.toString() : "");
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
            return toString;
        }
    }
}
