package io.t28.jsoon.core.builder;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

public abstract class ClassBuilder implements Builder<TypeSpec> {
    protected final TypeSpec.Builder classBuilder;
    protected final MethodSpec.Builder constructorBuilder;

    public ClassBuilder(@Nonnull String className, @Nonnull Modifier... modifiers) {
        this.classBuilder = TypeSpec.classBuilder(className).addModifiers(modifiers);
        this.constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
    }

    @Nonnull
    public ClassBuilder addClass(@Nonnull TypeSpec classSpec) {
        classBuilder.addType(classSpec);
        return this;
    }

    @Nonnull
    public abstract ClassBuilder addNull(@Nonnull String name);

    @Nonnull
    public abstract ClassBuilder addPrimitive(@Nonnull TypeName type, @Nonnull String name);

    @Nonnull
    public abstract ClassBuilder addArray(@Nonnull TypeName type, @Nonnull String name);

    @Nonnull
    public abstract ClassBuilder addObject(@Nonnull TypeName type, @Nonnull String name);

    @Nonnull
    public abstract ClassBuilder newClassBuilder(@Nonnull String className, @Nonnull Modifier... modifiers);

    @Nonnull
    @Override
    public TypeSpec build() {
        final MethodSpec constructor = constructorBuilder.build();
        return classBuilder.addMethod(constructor).build();
    }
}
