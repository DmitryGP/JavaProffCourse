package ru.dgp.clientservice.crm.transaction;

import java.util.function.Supplier;

public interface TransactionAction<T> extends Supplier<T> {}
