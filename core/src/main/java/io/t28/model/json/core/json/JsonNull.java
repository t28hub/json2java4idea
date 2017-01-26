package io.t28.model.json.core.json;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonNull extends JsonValue {
    @Nonnull
    @Override
    public TypeName getType() {
        return TypeName.OBJECT;
    }

    @Nullable
    @Override
    public Object getValue() {
        return null;
    }
}
