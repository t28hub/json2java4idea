package io.t28.json2java.core;

import com.google.common.annotations.VisibleForTesting;
import com.squareup.javapoet.*;
import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.json.JsonArray;
import io.t28.json2java.core.json.JsonNull;
import io.t28.json2java.core.json.JsonObject;
import io.t28.json2java.core.json.JsonValue;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;

public class JavaConverter {
    private final Configuration configuration;

    public JavaConverter(@Nonnull Configuration configuration) {
        this.configuration = configuration;
    }

    @Nonnull
    @CheckReturnValue
    public String convert(@Nonnull String packageName, @Nonnull String className, @Nonnull String json) throws IOException {
        final JsonValue value = configuration.jsonParser().parse(json);
        // Adding root class specific annotations
        final TypeSpec typeSpec = fromValue(className, value)
                .toBuilder()
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "$S", "all")
                        .build())
                .addAnnotation(AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", getClass().getCanonicalName())
                        .build())
                .build();
        return configuration.javaBuilder().build(packageName, typeSpec);
    }

    @Nonnull
    @CheckReturnValue
    @VisibleForTesting
    TypeSpec fromValue(@Nonnull String className, @Nonnull JsonValue value) {
        if (value.isObject()) {
            return fromObject(className, value.asObject(), Modifier.PUBLIC);
        }

        if (value.isArray()) {
            return fromArray(className, value.asArray());
        }

        throw new IllegalArgumentException("value must be an Object or Array");
    }

    @Nonnull
    private TypeSpec fromObject(@Nonnull String className, @Nonnull JsonObject object, @Nonnull Modifier... modifiers) {
        final NamePolicy classNamePolicy = configuration.classNamePolicy();
        final ClassBuilder builder = configuration.classBuilder();
        builder.addModifiers(modifiers);
        object.stream().forEach(child -> {
            final String key = child.getKey();
            final JsonValue value = child.getValue();
            if (value.isObject()) {
                final String innerClassName = classNamePolicy.convert(key, TypeName.OBJECT);
                final TypeSpec innerClass = fromObject(innerClassName, value.asObject(), Modifier.PUBLIC, Modifier.STATIC);
                builder.addInnerType(innerClass);

                final TypeName innerClassType = ClassName.bestGuess(innerClassName);
                builder.addProperty(key, innerClassType);
                return;
            }

            if (value.isArray()) {
                final String innerClassName = classNamePolicy.convert(key, TypeName.OBJECT);
                final JsonValue firstValue = value.asArray().stream().findFirst().orElse(new JsonNull());
                final TypeName listType = generateListType(innerClassName, firstValue, builder);
                builder.addProperty(key, listType);
                return;
            }

            builder.addProperty(key, value.getType());
        });
        return builder.build(className);
    }

    @Nonnull
    private TypeSpec fromArray(@Nonnull String className, @Nonnull JsonArray array) {
        final JsonValue firstValue = array.stream().findFirst().orElse(new JsonNull());
        if (firstValue.isObject()) {
            return fromValue(className, firstValue.asObject());
        }

        if (firstValue.isArray()) {
            return fromArray(className, firstValue.asArray());
        }

        throw new IllegalArgumentException("Cannot create class from empty array or primitive array");
    }

    @Nonnull
    private TypeName generateListType(@Nonnull String className, @Nonnull JsonValue value, @Nonnull ClassBuilder builder) {
        if (value.isArray()) {
            final JsonValue firstValue = value.asArray()
                    .stream()
                    .findFirst()
                    .orElse(new JsonNull());
            final TypeName type = generateListType(className, firstValue, builder);
            return ParameterizedTypeName.get(ClassName.get(List.class), type);
        }

        if (value.isObject()) {
            final TypeSpec innerClass = fromObject(className, value.asObject(), Modifier.PUBLIC, Modifier.STATIC);
            builder.addInnerType(innerClass);

            final TypeName innerClassType = ClassName.bestGuess(innerClass.name);
            return ParameterizedTypeName.get(ClassName.get(List.class), innerClassType);
        }

        return ParameterizedTypeName.get(ClassName.get(List.class), value.getType().box());
    }
}
