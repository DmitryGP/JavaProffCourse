package ru.dgp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dgp.annotations.After;
import ru.dgp.annotations.Before;
import ru.dgp.annotations.Test;
import ru.dgp.framework.TestLauncher;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ClassNotFoundException {

        TestLauncher.launch(Main.class.getName());
    }

    @Test
    public void test1() {
        logger.info("launching test1");
    }

    @Test
    public void test2() {
        logger.info("launching test2");
        throw new IllegalStateException();
    }

    @Before
    public void setup1() {
        logger.info("Setup...");
    }

    @Before
    public void setup2() {
        logger.info("More setup...");
    }

    @After
    public void tearDown1() {
        logger.info("Clearing...");
    }

    @After
    public void tearDown2() {
        logger.info("More clearing...");
    }
}
