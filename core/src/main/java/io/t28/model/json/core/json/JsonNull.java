package io.t28.model.json.core.json;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;

public class JsonNull extends JsonValue<Object> {
    @Nonnull
    @Override
    public TypeName getType() {
        return TypeName.OBJECT;
    }

    @Nonnull
    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("JsonNull does not support to return value due to null");
    }
}
