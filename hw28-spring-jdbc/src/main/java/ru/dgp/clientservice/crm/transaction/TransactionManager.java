package ru.dgp.clientservice.crm.transaction;

public interface TransactionManager {

    <T> T doInTransaction(TransactionAction<T> action);
}
