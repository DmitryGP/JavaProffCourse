package ru.dgp.appcontainer;

public class AppComponentContainerException extends RuntimeException {
    public AppComponentContainerException(String message) {
        super(message);
    }

    public AppComponentContainerException(Exception exc) {
        super(exc);
    }
}
