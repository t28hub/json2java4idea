package io.t28.jsoon.core.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

public class ModelClassBuilder extends ClassBuilder {
    public ModelClassBuilder(@Nonnull String className, @Nonnull Modifier... modifiers) {
        super(className, modifiers);
    }

    @Nonnull
    @Override
    public ClassBuilder addNull(@Nonnull String name) {
        return add(TypeName.OBJECT, name);
    }

    @Nonnull
    @Override
    public ClassBuilder addPrimitive(@Nonnull TypeName type, @Nonnull String name) {
        return add(type, name);
    }

    @Nonnull
    @Override
    public ClassBuilder addArray(@Nonnull TypeName type, @Nonnull String name) {
        return add(type, name);
    }

    @Nonnull
    @Override
    public ClassBuilder addObject(@Nonnull TypeName type, @Nonnull String name) {
        return add(type, name);
    }

    @Nonnull
    @Override
    public ClassBuilder newClassBuilder(@Nonnull String className, @Nonnull Modifier... modifiers) {
        return new ModelClassBuilder(className, modifiers);
    }

    @Nonnull
    private ClassBuilder add(@Nonnull TypeName type, @Nonnull String name) {
        constructorBuilder.addParameter(ParameterSpec.builder(type, name)
                .build())
                .addStatement("this.$1L = $1L", name);
        classBuilder.addField(FieldSpec.builder(type, name)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        classBuilder.addMethod(MethodSpec.methodBuilder(name)
                .returns(type)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $L", name)
                .build());
        return this;
    }
}
