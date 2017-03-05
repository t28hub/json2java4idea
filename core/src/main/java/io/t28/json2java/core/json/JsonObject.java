package io.t28.json2java.core.json;

import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class JsonObject extends JsonValue {
    private final Map<String, Object> value;

    public JsonObject(@Nonnull Map<String, Object> value) {
        this.value = new LinkedHashMap<>(value);
    }

    @Nonnull
    @Override
    public TypeName getType() {
        return ParameterizedTypeName.get(Map.class, String.class, Object.class);
    }

    @Nonnull
    @Override
    public Map<String, Object> getValue() {
        return ImmutableMap.copyOf(value);
    }

    @Nonnull
    @CheckReturnValue
    public Stream<Map.Entry<String, JsonValue>> stream() {
        return value.entrySet()
                .stream()
                .map(entry -> {
                    final String name = entry.getKey();
                    final Object value = entry.getValue();
                    return new HashMap.SimpleImmutableEntry<>(name, wrap(value));
                });
    }
}
