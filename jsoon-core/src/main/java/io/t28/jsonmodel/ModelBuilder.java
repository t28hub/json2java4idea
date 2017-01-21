package io.t28.jsonmodel;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import java.util.List;

public class ModelBuilder implements ClassBuilder {
    private final String modelName;

    public ModelBuilder(@Nonnull String modelName) {
        this.modelName = modelName;
    }

    @Nonnull
    @Override
    public TypeSpec build(@Nonnull JsonElement element) {
        final ModelVisitor visitor = new ModelVisitor(
                TypeSpec.classBuilder(modelName)
                        .addModifiers(Modifier.PUBLIC),
                MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
        );

        if (element.isObject()) {
            ((JsonObject) element).forEach(child -> child.accept(visitor));
        } else {
            element.accept(visitor);
        }
        return visitor.build();
    }

    public static class ModelVisitor implements Visitor {
        private final TypeSpec.Builder classBuilder;
        private final MethodSpec.Builder constructorBuilder;

        public ModelVisitor(@Nonnull TypeSpec.Builder classBuilder, @Nonnull MethodSpec.Builder constructorBuilder) {
            this.classBuilder = classBuilder;
            this.constructorBuilder = constructorBuilder;
        }

        @Override
        public void visit(@Nonnull JsonNull element) {
            addProperty(TypeName.get(Object.class), element.getName());
        }

        @Override
        public void visit(@Nonnull JsonBoolean element) {
            addProperty(TypeName.BOOLEAN, element.getName());
        }

        @Override
        public void visit(@Nonnull JsonNumber element) {
            addProperty(element.getTypeName(), element.getName());
        }

        @Override
        public void visit(@Nonnull JsonString element) {
            addProperty(TypeName.get(String.class), element.getName());
        }

        @Override
        public void visit(@Nonnull JsonArray element) {
            final TypeName array = createArray(element);
            addProperty(array, element.getName());
        }

        @Override
        public void visit(@Nonnull JsonObject element) {
            final TypeName enclosedClass = createEnclosedObject(element);
            addProperty(enclosedClass, element.getName());
        }

        @Nonnull
        TypeSpec build() {
            return classBuilder
                    .addMethod(constructorBuilder.build())
                    .build();
        }

        private void addProperty(@Nonnull TypeName type, @Nonnull String name) {
            constructorBuilder.addParameter(ParameterSpec.builder(type, name).build())
                    .addStatement("this.$1L = $1L", name);
            classBuilder.addField(FieldSpec.builder(type, name)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .build());
            classBuilder.addMethod(MethodSpec.methodBuilder(name)
                    .returns(type)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return $L", name)
                    .build());
        }

        @Nonnull
        private TypeName createArray(@Nullable JsonElement element) {
            if (element instanceof JsonBoolean) {
                return ParameterizedTypeName.get(List.class, Boolean.class);
            }

            if (element instanceof JsonNumber) {
                return ParameterizedTypeName.get(ClassName.get(List.class), ((JsonNumber) element).getBoxedTypeName());
            }

            if (element instanceof JsonString) {
                return ParameterizedTypeName.get(List.class, String.class);
            }

            if (element instanceof JsonArray) {
                final JsonArray array = (JsonArray) element;
                final TypeName typeName = createArray(array.findFirst().orElse(null));
                return ParameterizedTypeName.get(ClassName.get(List.class), typeName);
            }

            if (element instanceof JsonObject) {
                final JsonObject object = (JsonObject) element;
                final TypeName enclosedClassName = createEnclosedObject(object);
                return ParameterizedTypeName.get(ClassName.get(List.class), enclosedClassName);
            }
            return ParameterizedTypeName.get(List.class, Object.class);
        }

        @Nonnull
        private TypeName createEnclosedObject(@Nonnull JsonObject object) {
            final String name = object.getName();
            final String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
            final ModelVisitor visitor = new ModelVisitor(
                    TypeSpec.classBuilder(className)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC),
                    MethodSpec.constructorBuilder()
                            .addModifiers(Modifier.PUBLIC)
            );
            object.forEach(child -> child.accept(visitor));

            final TypeSpec enclosedClass = visitor.build();
            classBuilder.addType(enclosedClass);

            return ClassName.bestGuess(enclosedClass.name);
        }
    }
}
