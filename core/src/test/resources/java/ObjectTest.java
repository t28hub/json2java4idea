package io.t28.test;

import java.util.List;
import javax.annotation.Generated;

@Generated("io.t28.pojojson.core.PojoJson")
@SuppressWarnings("all")
public class ObjectTest {
    private final int numberValue;

    private final String stringValue;

    private final Object nullValue;

    private final List<String> arrayValue;

    private final ObjectValue objectValue;

    public ObjectTest(int numberValue, String stringValue, Object nullValue,
            List<String> arrayValue, ObjectValue objectValue) {
        this.numberValue = numberValue;
        this.stringValue = stringValue;
        this.nullValue = nullValue;
        this.arrayValue = arrayValue;
        this.objectValue = objectValue;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Object getNullValue() {
        return nullValue;
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
