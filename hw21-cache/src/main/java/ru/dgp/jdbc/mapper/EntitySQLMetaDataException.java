package ru.dgp.jdbc.mapper;

public class EntitySQLMetaDataException extends RuntimeException {
    public EntitySQLMetaDataException(Exception exc) {
        super(exc);
    }

    public EntitySQLMetaDataException(String message) {
        super(message);
    }
}
