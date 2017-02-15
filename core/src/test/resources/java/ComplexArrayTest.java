package io.t28.test;

import java.util.List;

public class ComplexArrayTest {
    private final List<Integer> array;

    private final List<Objects> objects;

    private final String name;

    private final List<List<Object>> nested;

    public ComplexArrayTest(List<Integer> array, List<Objects> objects, String name,
            List<List<Object>> nested) {
        this.array = array;
        this.objects = objects;
        this.name = name;
        this.nested = nested;
    }

    public List<Integer> getArray() {
        return array;
    }

    public List<Objects> getObjects() {
        return objects;
    }

    public String getName() {
        return name;
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
