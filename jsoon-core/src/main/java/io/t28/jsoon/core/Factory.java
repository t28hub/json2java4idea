package io.t28.jsoon.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.google.common.io.Files;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Factory {
    private final ObjectMapper mapper;

    public Factory(@Nonnull ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Nonnull
    public String create(@Nonnull String packageName, @Nonnull String className, @Nonnull String json) throws Exception {
        final JsonNode rootNode = mapper.readTree(json);
        if (!rootNode.isObject() && !rootNode.isArray()) {
            throw new IllegalArgumentException();
        }

        final TypeSpec typeSpec = create(className, rootNode)
                .toBuilder()
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "$S", "all")
                        .build())
                .build();
        final JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent("    ")
                .skipJavaLangImports(true)
                .build();
        return javaFile.toString();
    }

    @Nonnull
    private TypeSpec create(String name, JsonNode node) {
        final String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addAnnotation(AnnotationSpec.builder(JsonIgnoreProperties.class)
                        .addMember("ignoreUnknown", "$L", true)
                        .build());
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(JsonCreator.class)
                        .build());
        final Iterable<Map.Entry<String, JsonNode>> fields = node::fields;
        fields.forEach(field -> {
            final String childName = field.getKey();
            final JsonNode childNode = field.getValue();

            if (childNode.isObject()) {
                final TypeSpec enclosedClass = create(childName, childNode);
                final TypeName enclosedClassName = ClassName.bestGuess(enclosedClass.name);
                classBuilder.addType(enclosedClass);
                appendProperty(enclosedClassName, childName, classBuilder, constructorBuilder);
                return;
            }

            if (childNode.isBoolean()) {
                appendProperty(boolean.class, childName, classBuilder, constructorBuilder);
                return;
            }
            if (childNode.isInt()) {
                appendProperty(int.class, childName, classBuilder, constructorBuilder);
                return;
            }
            if (childNode.isLong()) {
                appendProperty(long.class, childName, classBuilder, constructorBuilder);
                return;
            }
            if (childNode.isFloat()) {
                appendProperty(float.class, childName, classBuilder, constructorBuilder);
                return;
            }
            if (childNode.isDouble()) {
                appendProperty(double.class, childName, classBuilder, constructorBuilder);
                return;
            }
            if (childNode.isTextual()) {
                appendProperty(String.class, childName, classBuilder, constructorBuilder);
                return;
            }
        });
        classBuilder.addMethod(constructorBuilder.build());
        return classBuilder.build();
    }

    private void appendProperty(@Nonnull TypeName type, @Nonnull String childName, @Nonnull TypeSpec.Builder classBuilder, @Nonnull MethodSpec.Builder constructorBuilder) {
        constructorBuilder.addParameter(ParameterSpec.builder(type, childName)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", childName)
                        .build())
                .build())
                .addStatement("this.$1L = $1L", childName);
        classBuilder.addField(FieldSpec.builder(type, childName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        classBuilder.addMethod(MethodSpec.methodBuilder(childName)
                .returns(type)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", childName)
                        .build())
                .addStatement("return $L", childName)
                .build());
    }

    private void appendProperty(@Nonnull Type type, @Nonnull String childName, @Nonnull TypeSpec.Builder classBuilder, @Nonnull MethodSpec.Builder constructorBuilder) {
        constructorBuilder.addParameter(ParameterSpec.builder(type, childName)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", childName)
                        .build())
                .build())
                .addStatement("this.$1L = $1L", childName);
        classBuilder.addField(FieldSpec.builder(type, childName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        classBuilder.addMethod(MethodSpec.methodBuilder(childName)
                .returns(type)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", childName)
                        .build())
                .addStatement("return $L", childName)
                .build());
    }

    public static void main(String[] args) throws Exception {
        final File file = new File("jsoon-core/src/main/resources/issue.json");
        final String json = Files.toString(file, StandardCharsets.UTF_8);
        final Factory factory = new Factory(new ObjectMapper());
        final String javaFile = factory.create("io.t28.jsoon.example", "Example", json);
        System.out.println(javaFile);
    }
}
