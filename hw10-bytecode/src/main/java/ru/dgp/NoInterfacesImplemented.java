package ru.dgp;

public class NoInterfacesImplemented extends Throwable {

    public NoInterfacesImplemented() {
        super("Class doesn't implement any interface.");
    }
}
