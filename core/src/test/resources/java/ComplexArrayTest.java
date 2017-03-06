package io.t28.test;

import java.util.List;
import javax.annotation.Generated;

@SuppressWarnings("all")
@Generated("io.t28.json2java.core.JavaConverter")
public class ComplexArrayTest {
    private final String name;

    private final List<Integer> array;

    private final List<Objects> objects;

    private final List<List<Object>> nested;

    public ComplexArrayTest(String name, List<Integer> array, List<Objects> objects,
            List<List<Object>> nested) {
        this.name = name;
        this.array = array;
        this.objects = objects;
        this.nested = nested;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getArray() {
        return array;
    }

    public List<Objects> getObjects() {
        return objects;
    }

    public List<List<Object>> getNested() {
        return nested;
    }

    public static class Objects {
        private final String key;

        public Objects(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
