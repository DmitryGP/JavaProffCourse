package org.dgp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        if (data == null) {
            return "";
        }

        return Arrays.toString(data.toArray());
    }

    public static ObjectForMessage clone(ObjectForMessage o) {
        if (o != null) {
            ObjectForMessage clone = new ObjectForMessage();

            clone.setData(new ArrayList<>(List.copyOf(o.data)));

            return clone;
        }

        return null;
    }
}
