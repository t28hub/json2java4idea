package io.t28.jsoon.core;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.jsoon.core.builder.ClassBuilder;
import io.t28.jsoon.core.json.JsonArray;
import io.t28.jsoon.core.json.JsonElement;
import io.t28.jsoon.core.json.JsonNull;
import io.t28.jsoon.core.json.JsonObject;
import io.t28.jsoon.core.json.JsonPrimitive;
import io.t28.jsoon.core.json.Visitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import java.util.List;

public class JsonVisitor implements Visitor {
    private final ClassBuilder builder;

    public JsonVisitor(@Nonnull ClassBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void visit(@Nonnull JsonNull element) {
        builder.addNull(element.getName());
    }

    @Override
    public void visit(@Nonnull JsonPrimitive element) {
        builder.addPrimitive(element.getTypeName(), element.getName());
    }

    @Override
    public void visit(@Nonnull JsonArray element) {
        final String name = element.getName();
        final JsonElement firstElement = element.stream().findFirst().orElse(null);
        final TypeName valueType = createList(name, firstElement);
        builder.addArray(valueType, element.getName());
    }

    @Override
    public void visit(@Nonnull JsonObject element) {
        final String name = element.getName();
        final TypeName valueType = createEnclosedClass(name, element);
        builder.addObject(valueType, name);
    }

    @Nonnull
    private TypeName createList(@Nonnull String name, @Nullable JsonElement element) {
        if (element == null || element.isNull()) {
            return ParameterizedTypeName.get(List.class, Object.class);
        }

        if (element.isPrimitive()) {
            final TypeName valueType = element.asPrimitive().getBoxedTypeName();
            return ParameterizedTypeName.get(ClassName.get(List.class), valueType);
        }

        if (element.isArray()) {
            final JsonArray array = element.asArray();
            final JsonElement firstElement = array.stream().findFirst().orElse(null);
            final TypeName nestedValueType = createList(name, firstElement);
            return ParameterizedTypeName.get(ClassName.get(List.class), nestedValueType);
        }

        final TypeName valueType = createEnclosedClass(name + "Item", element.asObject());
        return ParameterizedTypeName.get(ClassName.get(List.class), valueType);
    }

    @Nonnull
    private TypeName createEnclosedClass(@Nonnull String name, @Nonnull JsonObject element) {
        final String enclosedClassName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
        final ClassBuilder enclosedBuilder = builder.newClassBuilder(enclosedClassName, Modifier.PUBLIC, Modifier.STATIC);
        final JsonVisitor enclosedVisitor = new JsonVisitor(enclosedBuilder);
        element.stream().forEach(child -> child.accept(enclosedVisitor));

        final TypeSpec enclosedClass = enclosedBuilder.build();
        builder.addClass(enclosedClass);

        return ClassName.bestGuess(enclosedClass.name);
    }
}
