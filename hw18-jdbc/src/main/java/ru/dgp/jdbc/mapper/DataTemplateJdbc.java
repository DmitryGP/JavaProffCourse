package ru.dgp.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
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
            var allFields = entityClassMetaData.getAllFields();
            var constructor = entityClassMetaData.getConstructor();

            try {
                if (rs.next()) {

                    var args = allFields.stream()
                            .map(f -> {
                                try {
                                    return rs.getObject(f.getName());
                                } catch (SQLException e) {
                                    throw new DataTemplateException(e);
                                }
                            })
                            .toList();

                    return constructor.newInstance(args.toArray());
                }

                return null;
            } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        var query = entitySQLMetaData.getSelectAllSql();

        return dbExecutor
                .executeSelect(connection, query, Collections.emptyList(), rs -> {
                    var allFields = entityClassMetaData.getAllFields();
                    var constructor = entityClassMetaData.getConstructor();

                    var entityList = new ArrayList<T>();

                    try {
                        while (rs.next()) {

                            var args = allFields.stream()
                                    .map(f -> {
                                        try {
                                            return rs.getObject(f.getName());
                                        } catch (SQLException e) {
                                            throw new DataTemplateException(e);
                                        }
                                    })
                                    .toList();

                            entityList.add(constructor.newInstance(args.toArray()));
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
    @SuppressWarnings("java:S3011")
    public long insert(Connection connection, T obj) {
        var query = entitySQLMetaData.getInsertSql();
        var fieldsToInsert = entityClassMetaData.getFieldsWithoutId();

        var paramValues = fieldsToInsert.stream()
                .map(f -> {
                    try {
                        f.setAccessible(true);
                        return f.get(obj);
                    } catch (IllegalAccessException e) {
                        throw new DataTemplateException(e);
                    } finally {
                        f.setAccessible(false);
                    }
                })
                .toList();

        return dbExecutor.executeStatement(connection, query, paramValues);
    }

    @Override
    @SuppressWarnings("java:S3011")
    public void update(Connection connection, T obj) {
        var query = entitySQLMetaData.getInsertSql();
        var fieldsToInsert = entityClassMetaData.getFieldsWithoutId();
        Object idValue;
        Field idField = entityClassMetaData.getIdField();

        try {
            idField.setAccessible(true);
            idValue = idField.get(obj);
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        } finally {
            idField.setAccessible(false);
        }

        var paramValues = fieldsToInsert.stream()
                .map(f -> {
                    try {
                        f.setAccessible(true);
                        return f.get(obj);
                    } catch (IllegalAccessException e) {
                        throw new DataTemplateException(e);
                    } finally {
                        f.setAccessible(false);
                    }
                })
                .toList();

        paramValues.add(idValue);

        dbExecutor.executeStatement(connection, query, paramValues);
    }
}
