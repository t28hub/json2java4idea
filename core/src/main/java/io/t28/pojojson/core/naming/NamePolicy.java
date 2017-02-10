package io.t28.pojojson.core.naming;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;

public interface NamePolicy {
    @Nonnull
    String convert(@Nonnull String name, @Nonnull TypeName type);
}
