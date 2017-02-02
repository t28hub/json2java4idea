package io.t28.pojojson.core.naming;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;

public interface NamingStrategy {
    @Nonnull
    String transform(@Nonnull String name, @Nonnull TypeName type);
}
