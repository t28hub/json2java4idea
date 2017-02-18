package io.t28.pojojson.core;

import com.google.common.annotations.VisibleForTesting;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.pojojson.core.builder.ClassBuilder;
import io.t28.pojojson.core.io.JavaBuilder;
import io.t28.pojojson.core.io.JsonParser;
import io.t28.pojojson.core.io.impl.JavaBuilderImpl;
import io.t28.pojojson.core.io.impl.JsonParserImpl;
import io.t28.pojojson.core.json.JsonArray;
import io.t28.pojojson.core.json.JsonNull;
import io.t28.pojojson.core.json.JsonObject;
import io.t28.pojojson.core.json.JsonValue;
import io.t28.pojojson.core.naming.DefaultNamePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.List;

public class PojoJson {
    private final Style style;
    private final NamePolicy classNamePolicy;
    private final NamePolicy fieldNamePolicy;
    private final NamePolicy methodNamePolicy;
    private final NamePolicy parameterNamePolicy;
    private final JsonParser jsonParser;
    private final JavaBuilder javaBuilder;

    private PojoJson(@Nonnull Builder builder) {
        style = builder.style;
        classNamePolicy = builder.classNamePolicy;
        fieldNamePolicy = builder.fieldNamePolicy;
        methodNamePolicy = builder.methodNamePolicy;
        parameterNamePolicy = builder.parameterNamePolicy;
        jsonParser = builder.jsonParser;
        javaBuilder = builder.javaBuilder;
    }

    @Nonnull
    @CheckReturnValue
    public static Builder builder() {
        return new Builder();
    }

    @Nonnull
    @CheckReturnValue
    public String generate(@Nonnull String packageName, @Nonnull String className, @Nonnull String json) throws IOException {
        final JsonValue value = jsonParser.parse(json);
        // Adding root class specific annotations
        final TypeSpec typeSpec = fromValue(className, value)
                .toBuilder()
                .addAnnotation(AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", getClass().getCanonicalName())
                        .build())
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "$S", "all")
                        .build())
                .build();
        return javaBuilder.build(packageName, typeSpec);
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
        final ClassBuilder builder = style.toBuilder(fieldNamePolicy, methodNamePolicy, parameterNamePolicy);
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

    public static class Builder {
        private Style style;
        private NamePolicy classNamePolicy;
        private NamePolicy fieldNamePolicy;
        private NamePolicy methodNamePolicy;
        private NamePolicy parameterNamePolicy;
        private JsonParser jsonParser;
        private JavaBuilder javaBuilder;

        private Builder() {
            style = Style.NONE;
            classNamePolicy = DefaultNamePolicy.CLASS;
            fieldNamePolicy = DefaultNamePolicy.FIELD;
            methodNamePolicy = DefaultNamePolicy.METHOD;
            parameterNamePolicy = DefaultNamePolicy.PARAMETER;
            jsonParser = new JsonParserImpl();
            javaBuilder = new JavaBuilderImpl();
        }

        @Nonnull
        @CheckReturnValue
        public Builder style(@Nonnull Style style) {
            this.style = style;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder classNamePolicy(@Nonnull NamePolicy classNamePolicy) {
            this.classNamePolicy = classNamePolicy;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder fieldNamePolicy(@Nonnull NamePolicy fieldNamePolicy) {
            this.fieldNamePolicy = fieldNamePolicy;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder methodNamePolicy(@Nonnull NamePolicy methodNamePolicy) {
            this.methodNamePolicy = methodNamePolicy;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder parameterNamePolicy(@Nonnull NamePolicy parameterNamePolicy) {
            this.parameterNamePolicy = parameterNamePolicy;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        @VisibleForTesting
        Builder jsonParser(@Nonnull JsonParser jsonParser) {
            this.jsonParser = jsonParser;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        @VisibleForTesting
        Builder javaBuilder(@Nonnull JavaBuilder javaBuilder) {
            this.javaBuilder = javaBuilder;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public PojoJson build() {
            return new PojoJson(this);
        }
    }
}
