package io.t28.test;

import java.util.List;
import javax.annotation.Generated;

@SuppressWarnings("all")
@Generated("io.t28.json2java.core.JavaConverter")
public class ObjectTest {
    private final Object nullValue;

    private final int numberValue;

    private final String stringValue;

    private final List<String> arrayValue;

    private final ObjectValue objectValue;

    public ObjectTest(Object nullValue, int numberValue, String stringValue,
            List<String> arrayValue, ObjectValue objectValue) {
        this.nullValue = nullValue;
        this.numberValue = numberValue;
        this.stringValue = stringValue;
        this.arrayValue = arrayValue;
        this.objectValue = objectValue;
    }

    public Object getNullValue() {
        return nullValue;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public List<String> getArrayValue() {
        return arrayValue;
    }

    public ObjectValue getObjectValue() {
        return objectValue;
    }

    public static class ObjectValue {
        private final String name;

        public ObjectValue(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
