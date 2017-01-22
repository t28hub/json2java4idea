package io.t28.jsoon.core;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

public class ClassBuilder {
    private final TypeSpec.Builder classBuilder;
    private final MethodSpec.Builder constructorBuilder;

    public ClassBuilder(@Nonnull String className) {
        this(TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC), MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC));
    }

    ClassBuilder(@Nonnull TypeSpec.Builder classBuilder, @Nonnull MethodSpec.Builder constructorBuilder) {
        this.classBuilder = classBuilder;
        this.constructorBuilder = constructorBuilder;
    }

    @Nonnull
    public TypeSpec build() {
        return classBuilder
                .addMethod(constructorBuilder.build())
                .build();
    }
}
