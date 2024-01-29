package ru.dgp.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.dgp.core.repository.DataTemplate;
import ru.dgp.core.repository.DataTemplateException;
import ru.dgp.core.repository.executor.DbExecutor;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        var query = entitySQLMetaData.getSelectByIdSql();

        return dbExecutor.executeSelect(connection, query, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createInstance(rs);
                }

                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    private <T> T createInstance(ResultSet rs)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        var allFields = entityClassMetaData.getAllFields();
        var constructor = entityClassMetaData.getConstructor();

        var args = allFields.stream().map(f -> getResultSetValue(rs, f)).toList();

        return (T) constructor.newInstance(args.toArray());
    }

    private static Object getResultSetValue(ResultSet rs, Field f) {
        try {
            return rs.getObject(f.getName());
        } catch (SQLException e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public List<T> findAll(Connection connection) {
        var query = entitySQLMetaData.getSelectAllSql();

        return dbExecutor
                .executeSelect(connection, query, Collections.emptyList(), rs -> {
                    var entityList = new ArrayList<T>();

                    try {
                        while (rs.next()) {

                            entityList.add(createInstance(rs));
                        }

                        return entityList;
                    } catch (SQLException
                            | InstantiationException
                            | IllegalAccessException
                            | InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new DataTemplateException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T obj) {
        var query = entitySQLMetaData.getInsertSql();
        var fieldsToInsert = entityClassMetaData.getFieldsWithoutId();

        var paramValues =
                fieldsToInsert.stream().map(f -> getFieldValue(f, obj)).toList();

        return dbExecutor.executeStatement(connection, query, paramValues);
    }

    @Override
    public void update(Connection connection, T obj) {
        var query = entitySQLMetaData.getInsertSql();
        var fieldsToInsert = entityClassMetaData.getFieldsWithoutId();
        Object idValue;
        Field idField = entityClassMetaData.getIdField();

        idValue = getFieldValue(idField, obj);

        var paramValues =
                fieldsToInsert.stream().map(f -> getFieldValue(f, obj)).toList();

        paramValues.add(idValue);

        dbExecutor.executeStatement(connection, query, paramValues);
    }

    @SuppressWarnings("java:S3011")
    private static Object getFieldValue(Field field, Object obj) {
        Object idValue;
        try {
            field.setAccessible(true);
            idValue = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        } finally {
            field.setAccessible(false);
        }
        return idValue;
    }
}
