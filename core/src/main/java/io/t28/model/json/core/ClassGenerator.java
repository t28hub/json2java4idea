package io.t28.model.json.core;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.builder.ClassBuilder;
import io.t28.model.json.core.json.JsonArray;
import io.t28.model.json.core.json.JsonNull;
import io.t28.model.json.core.json.JsonObject;
import io.t28.model.json.core.json.JsonValue;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import java.util.List;

public class ClassGenerator {
    private final Context context;

    @Inject
    public ClassGenerator(@Nonnull Context context) {
        this.context = context;
    }

    @Nonnull
    public TypeSpec generate(@Nonnull String className, @Nonnull JsonValue value) {
        if (value.isObject()) {
            return generate(className, value.asObject(), Modifier.PUBLIC);
        }
        if (value.isArray()) {
            return generate(className, value.asArray());
        }
        throw new IllegalArgumentException("value must be an Array or Object");
    }

    @Nonnull
    private TypeSpec generate(@Nonnull String className, @Nonnull JsonObject object, @Nonnull Modifier... modifiers) {
        final ClassStyle classStyle = context.style();
        final ClassBuilder builder = classStyle.create(className, context);
        builder.addModifiers(modifiers);

        final NamingStrategy classNameStrategy = context.classNameStrategy();
        object.stream().forEach(child -> {
            final String name = child.getKey();
            final JsonValue value = child.getValue();
            if (value.isObject()) {
                final String innerClassName = classNameStrategy.transform(name, TypeName.OBJECT);
                final TypeSpec innerClass = generate(innerClassName, value.asObject(), Modifier.PUBLIC, Modifier.STATIC);
                builder.addInnerType(innerClass);

                final TypeName innerClassType = ClassName.bestGuess(innerClassName);
                builder.addProperty(name, innerClassType);
                return;
            }

            if (value.isArray()) {
                final String innerClassName = classNameStrategy.transform(name, TypeName.OBJECT);
                final JsonValue firstValue = value.asArray().stream().findFirst().orElse(new JsonNull());
                final TypeName listType = generateListType(innerClassName, firstValue, builder);
                builder.addProperty(name, listType);
                return;
            }

            builder.addProperty(name, value.getType());
        });
        return builder.build();
    }

    @Nonnull
    private TypeSpec generate(@Nonnull String className, @Nonnull JsonArray array) {
        final JsonValue firstValue = array.stream().findFirst().orElse(new JsonNull());
        if (firstValue.isObject()) {
            return generate(className, firstValue.asObject());
        }
        if (firstValue.isArray()) {
            return generate(className, firstValue.asArray());
        }
        throw new IllegalArgumentException();
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
            final TypeSpec innerClass = generate(className, value.asObject(), Modifier.PUBLIC, Modifier.STATIC);
            builder.addInnerType(innerClass);

            final TypeName innerClassType = ClassName.bestGuess(innerClass.name);
            return ParameterizedTypeName.get(ClassName.get(List.class), innerClassType);
        }

        return ParameterizedTypeName.get(ClassName.get(List.class), value.getType());
    }
}
