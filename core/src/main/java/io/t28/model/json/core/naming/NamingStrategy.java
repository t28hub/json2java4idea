package io.t28.model.json.core.naming;

import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;

public interface NamingStrategy {
    @Nonnull
    String apply(@Nonnull TypeName type, @Nonnull String name, @Nonnull NamingCase nameCase);
}
