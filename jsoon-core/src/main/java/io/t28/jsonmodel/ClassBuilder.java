package io.t28.jsonmodel;

import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;

public interface ClassBuilder {
    @Nonnull
    TypeSpec build(@Nonnull JsonElement element);
}
