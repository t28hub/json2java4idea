/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.json2java.core;

import com.google.common.annotations.VisibleForTesting;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.json2java.core.annotation.AnnotationPolicy;
import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.json.JsonArray;
import io.t28.json2java.core.json.JsonNull;
import io.t28.json2java.core.json.JsonObject;
import io.t28.json2java.core.json.JsonValue;
import io.t28.json2java.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
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
        final TypeSpec.Builder builder = fromValue(className, value).toBuilder();
        // Adding root class specific annotations
        for (final AnnotationPolicy policy : configuration.annotationPolicies()) {
            policy.apply(builder);
        }
        return configuration.javaBuilder().build(packageName, builder.build());
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
