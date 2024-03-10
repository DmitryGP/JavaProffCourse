package ru.dgp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadsApp {
    private static final Logger logger = LoggerFactory.getLogger(ThreadsApp.class);

    private int lastThreadNumber;

    public static void main(String[] args) {
        ThreadsApp threadsApp = new ThreadsApp();

        new Thread(() -> threadsApp.action(1)).start();
        new Thread(() -> threadsApp.action(2)).start();
    }

    @SuppressWarnings("java:S2189")
    private synchronized void action(int threadNum) {
        try {

            int count = 1;
            short delta = 1;

            while (true) {
                while (lastThreadNumber == threadNum) {
                    this.wait();
                }

                logger.atInfo()
                        .setMessage("Thread {}: {}")
                        .addArgument(threadNum)
                        .addArgument(count)
                        .log();

                if (count == 10) {
                    delta = -1;
                }

                if (count == 1) {
                    delta = 1;
                }

                count = count + delta;

                lastThreadNumber = threadNum;

                sleep();
                notifyAll();
            }

        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
