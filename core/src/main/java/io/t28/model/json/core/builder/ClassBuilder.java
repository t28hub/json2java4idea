package io.t28.model.json.core.builder;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

public interface ClassBuilder {
    @Nonnull
    ClassBuilder addModifiers(@Nonnull Modifier... modifiers);

    @Nonnull
    ClassBuilder addProperty(@Nonnull String name, @Nonnull TypeName type);

    @Nonnull
    ClassBuilder addEnclosedClass(@Nonnull TypeSpec enclosedClass);

    @Nonnull
    TypeSpec build();
}
