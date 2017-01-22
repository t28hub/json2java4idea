package io.t28.jsoon.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.io.Files;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.jsoon.core.json.JsonArray;
import io.t28.jsoon.core.json.JsonElement;
import io.t28.jsoon.core.json.JsonNull;
import io.t28.jsoon.core.json.JsonObject;
import io.t28.jsoon.core.json.JsonPrimitive;
import io.t28.jsoon.core.json.Visitor;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final File file = new File("core/src/main/resources/issue.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final ObjectMapper mapper = new ObjectMapper();

        final ModelClassBuilder builder = new ModelClassBuilder("Example", Modifier.PUBLIC);
        final JsonElement root = JsonElement.wrap(mapper.readTree(json));
        if (root.isObject()) {
            root.asObject()
                    .stream()
                    .forEach(child -> child.accept(builder));
        } else if (root.isArray()) {
            root.accept(builder);
        }

        final JavaFile javaFile = JavaFile.builder("io.t28.jsoon.core", builder.build())
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
        System.out.println(javaFile.toString());
    }

    public static class ModelClassBuilder implements Visitor {
        private final TypeSpec.Builder classBuilder;
        private final MethodSpec.Builder constructorBuilder;

        public ModelClassBuilder(@Nonnull String className, @Nonnull Modifier... modifiers) {
            this.classBuilder = TypeSpec.classBuilder(className).addModifiers(modifiers);
            this.constructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);
        }

        @Override
        public void visit(@Nonnull JsonNull element) {
            addProperty(TypeName.OBJECT, element.getName());
        }

        @Override
        public void visit(@Nonnull JsonPrimitive element) {
            addProperty(element.getTypeName(), element.getName());
        }

        @Override
        public void visit(@Nonnull JsonArray element) {
            final String name = element.getName();
            final JsonElement firstElement = element.stream().findFirst().orElse(null);
            final TypeName valueType = createList(name, firstElement);
            addProperty(valueType, name);
        }

        @Override
        public void visit(@Nonnull JsonObject element) {
            final String name = element.getName();
            final TypeName valueType = createClass(name, element);
            addProperty(valueType, name);
        }

        @Nonnull
        @CheckReturnValue
        public TypeSpec build() {
            return classBuilder.addMethod(constructorBuilder.build()).build();
        }

        @Nonnull
        private ModelClassBuilder addProperty(@Nonnull TypeName type, @Nonnull String name) {
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

            final TypeName valueType = createClass(name + "Item", element.asObject());
            return ParameterizedTypeName.get(ClassName.get(List.class), valueType);
        }

        @Nonnull
        private TypeName createClass(@Nonnull String name, @Nonnull JsonObject element) {
            final String enclosedClassName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
            final ModelClassBuilder enclosedClassBuilder = new ModelClassBuilder(enclosedClassName, Modifier.PUBLIC, Modifier.STATIC);
            element.stream().forEach(child -> child.accept(enclosedClassBuilder));

            final TypeSpec classSpec = enclosedClassBuilder.build();
            classBuilder.addType(classSpec);

            return ClassName.bestGuess(classSpec.name);
        }
    }
}
