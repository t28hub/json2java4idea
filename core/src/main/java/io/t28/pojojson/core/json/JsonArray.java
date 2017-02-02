package io.t28.pojojson.core.json;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public class JsonArray extends JsonValue {
    private final List<Object> value;

    public JsonArray(@Nonnull List<Object> value) {
        this.value = ImmutableList.copyOf(value);
    }

    @Nonnull
    @Override
    public TypeName getType() {
        return ParameterizedTypeName.get(List.class, Object.class);
    }

    @Nonnull
    @Override
    public List<Object> getValue() {
        return ImmutableList.copyOf(value);
    }

    @Nonnull
    @CheckReturnValue
    public Stream<JsonValue> stream() {
        return value.stream().map(JsonValue::wrap);
    }
}
