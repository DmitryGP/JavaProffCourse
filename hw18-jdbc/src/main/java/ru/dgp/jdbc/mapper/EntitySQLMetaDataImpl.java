package ru.dgp.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.IntStream;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {

        var tableName = entityClassMetaData.getName();
        var allFieldNames =
                entityClassMetaData.getAllFields().stream().map(Field::getName).toList();

        return "select %s form %s".formatted(String.join(", ", allFieldNames), tableName);
    }

    @Override
    public String getSelectByIdSql() {
        var idField = entityClassMetaData.getIdField();

        if (idField == null) {
            throw new EntitySQLMetaDataException("No id field.");
        }

        var tableName = entityClassMetaData.getName();
        var fieldNames =
                entityClassMetaData.getAllFields().stream().map(Field::getName).toList();

        var idFieldName = idField.getName();

        return "select %s from %s where %s = ?".formatted(String.join(", ", fieldNames), tableName, idFieldName);
    }

    @Override
    public String getInsertSql() {
        var tableName = entityClassMetaData.getName();
        var fieldNames = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .toList();

        var columnNames = String.join(", ", fieldNames);
        var replaces = IntStream.range(0, fieldNames.size()).mapToObj(i -> "?").toList();
        var paramPlaces = String.join(", ", replaces);

        return "insert into %s (%s) values (%s)".formatted(tableName, columnNames, paramPlaces);
    }

    @Override
    public String getUpdateSql() {
        var idField = entityClassMetaData.getIdField();

        if (idField == null) {
            throw new EntitySQLMetaDataException("No id field.");
        }

        var tableName = entityClassMetaData.getName();
        var fieldNames = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .toList();

        var idFieldName = idField.getName();

        var sets = fieldNames.stream().map("%s = ?"::formatted).toList();
        var params = String.join(", ", sets);

        return "update %s set %s where %s = ?".formatted(tableName, params, idFieldName);
    }
}
