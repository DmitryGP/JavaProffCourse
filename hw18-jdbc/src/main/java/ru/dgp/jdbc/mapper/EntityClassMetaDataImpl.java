package ru.dgp.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    private final String name;
    private final Constructor<T> constructor;
    private final List<Field> fieldsWithoutId;
    private final List<Field> allFields;
    private final Field idField;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;

        this.name = clazz.getSimpleName();
        this.constructor = getConstructor(clazz);
        this.idField = getIdField(clazz);
        this.allFields = new ArrayList<>(List.of(clazz.getDeclaredFields()));
        this.fieldsWithoutId = getFieldsWithoutId(clazz);
    }

    private List<Field> getFieldsWithoutId(Class<T> clazz) {
        List<Field> fieldsWithoutId;
        var declaredFields = clazz.getDeclaredFields();
        fieldsWithoutId = Arrays.stream(declaredFields)
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .toList();
        return fieldsWithoutId;
    }

    private Field getIdField(Class<T> clazz) {
        Field idField;
        var fields = clazz.getDeclaredFields();
        var idFld = Arrays.stream(fields)
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst();
        idField = idFld.orElseThrow(() -> new EntityClassMetaDataException("No id field."));

        return idField;
    }

    private Constructor<T> getConstructor(Class<T> clazz) {
        Constructor<T> constructor;
        var cnstrs = clazz.getConstructors();

        Constructor<?> cnstr = null;
        var paramsCount = -1;

        for (Constructor<?> c : cnstrs) {
            if (c.getParameterCount() > paramsCount) {
                paramsCount = c.getParameterCount();

                cnstr = c;
            }
        }

        constructor = (Constructor<T>) cnstr;
        return constructor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
