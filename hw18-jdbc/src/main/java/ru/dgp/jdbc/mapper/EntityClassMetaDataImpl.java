package ru.dgp.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    Class<T> clazz;

    private String name;
    private Constructor<T> constructor;
    private List<Field> fieldsWithoutId;
    private List<Field> allFields;
    private Field idField;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        if (name == null) {
            name = clazz.getSimpleName();
        }

        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {

        if (this.constructor == null) {
            var cnstrs = clazz.getConstructors();

            Constructor<?> cnstr = null;
            var paramsCount = -1;

            for (Constructor<?> c : cnstrs) {
                if (c.getParameterCount() > paramsCount) {
                    paramsCount = c.getParameterCount();

                    cnstr = c;
                }
            }

            this.constructor = (Constructor<T>) cnstr;
        }

        return constructor;
    }

    @Override
    public Field getIdField() {

        if (idField == null) {
            var fields = clazz.getDeclaredFields();
            var idFld = Arrays.stream(fields)
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst();
            idField = idFld.orElseThrow(() -> new EntityClassMetaDataException("No id field."));
        }

        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields == null) {
            allFields = new ArrayList<>(List.of(clazz.getDeclaredFields()));
        }

        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            var declaredFields = clazz.getDeclaredFields();
            fieldsWithoutId = Arrays.stream(declaredFields)
                    .filter(f -> !f.isAnnotationPresent(Id.class))
                    .toList();
        }

        return fieldsWithoutId;
    }
}
