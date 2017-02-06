package io.t28.pojojson.idea;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public enum Type {
    NONE, GSON, JACKSON;

    @NotNull
    public static Type fromName(@Nullable String name) {
        return Stream.of(Type.values())
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("This(" + name + ") is unknown name"));
    }
}
