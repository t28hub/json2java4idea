package io.t28.pojojson.core.json;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;

public class JsonString extends JsonValue {
    private final String value;

    public JsonString(@Nonnull String value) {
        this.value = value;
    }

    @Nonnull
    @Override
    public TypeName getType() {
        return ClassName.get(String.class);
    }

    @Nonnull
    @Override
    public String getValue() {
        return value;
    }
}
