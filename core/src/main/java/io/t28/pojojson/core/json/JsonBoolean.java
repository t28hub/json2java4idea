package io.t28.pojojson.core.json;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;

public class JsonBoolean extends JsonValue {
    private final boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    @Nonnull
    @Override
    public TypeName getType() {
        return TypeName.BOOLEAN;
    }

    @Nonnull
    @Override
    public Boolean getValue() {
        return value;
    }
}
