package io.t28.json2java.core.json;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public class JsonNumber extends JsonValue {
    private final Type type;
    private final Number value;

    public JsonNumber(@Nonnull Type type, @Nonnull Number value) {
        this.type = type;
        this.value = value;
    }

    @Nonnull
    @Override
    public TypeName getType() {
        return TypeName.get(type);
    }

    @Nonnull
    @Override
    public Number getValue() {
        return value;
    }

    @Nonnull
    public TypeName getBoxedType() {
        return getType().box();
    }
}
