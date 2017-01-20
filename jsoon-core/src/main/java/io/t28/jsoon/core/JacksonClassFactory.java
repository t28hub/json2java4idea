package io.t28.jsoon.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.jsoon.core.element.ArrayElement;
import io.t28.jsoon.core.element.JsonElement;
import io.t28.jsoon.core.element.NullElement;
import io.t28.jsoon.core.element.ObjectElement;
import io.t28.jsoon.core.element.PrimitiveElement;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.List;

public class JacksonClassFactory implements ClassFactory, Visitor {
    private final TypeSpec.Builder classBuilder;
    private final MethodSpec.Builder constructorBuilder;

    public JacksonClassFactory(final String className) {
        this.classBuilder = TypeSpec.classBuilder(className);
        this.constructorBuilder = MethodSpec.constructorBuilder();
    }

    @Nonnull
    @Override
    public TypeSpec create() {
        classBuilder.addMethod(constructorBuilder.build());
        return classBuilder.build();
    }

    @Override
    public void visit(@Nonnull PrimitiveElement element) {
        apply(element.getType(), element.getName());
    }

    @Override
    public void visit(@Nonnull ArrayElement element) {
        final JsonElement firstElement = element.getFirstElement().orElse(null);
        if (firstElement instanceof PrimitiveElement) {
            final PrimitiveElement primitiveElement = (PrimitiveElement) firstElement;
            final TypeName typeName = ParameterizedTypeName.get(ClassName.get(List.class), primitiveElement.getBoxedTypeName());
            apply(typeName, element.getName());
            return;
        }

        if (firstElement instanceof ObjectElement) {
            final String name = firstElement.getName();
            final String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
            final JacksonClassFactory factory = new JacksonClassFactory(className);
            firstElement.accept(factory);

            final TypeSpec enclosedClass = factory.create();
            final TypeName enclosedClassName = ClassName.bestGuess(enclosedClass.name);
            final TypeName fieldTypeName = ParameterizedTypeName.get(ClassName.get(List.class), enclosedClassName);
            apply(fieldTypeName, name);
            classBuilder.addType(enclosedClass);
            return;
        }

        final TypeName typeName = ParameterizedTypeName.get(List.class, Object.class);
        apply(typeName, element.getName());
    }

    @Override
    public void visit(@Nonnull ObjectElement element) {
        final String name = element.getName();
        final String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
        final JacksonClassFactory factory = new JacksonClassFactory(className);
        element.each(childElement -> childElement.accept(factory));

        final TypeSpec enclosedClass = factory.create();
        final TypeName enclosedTypeName = ClassName.bestGuess(enclosedClass.name);
        apply(enclosedTypeName, name);
        classBuilder.addType(enclosedClass);
    }

    @Override
    public void visit(@Nonnull NullElement element) {
        apply(Object.class, element.getName());
    }

    private void apply(@Nonnull TypeName type, @Nonnull String name) {
        constructorBuilder.addParameter(ParameterSpec.builder(type, name)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", name)
                        .build())
                .build())
                .addStatement("this.$1L = $1L", name);
        classBuilder.addField(FieldSpec.builder(type, name)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        classBuilder.addMethod(MethodSpec.methodBuilder(name)
                .returns(type)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", name)
                        .build())
                .addStatement("return $L", name)
                .build());
    }

    private void apply(@Nonnull Type type, @Nonnull String name) {
        constructorBuilder.addParameter(ParameterSpec.builder(type, name)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", name)
                        .build())
                .build())
                .addStatement("this.$1L = $1L", name);
        classBuilder.addField(FieldSpec.builder(type, name)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());
        classBuilder.addMethod(MethodSpec.methodBuilder(name)
                .returns(type)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(JsonProperty.class)
                        .addMember("value", "$S", name)
                        .build())
                .addStatement("return $L", name)
                .build());
    }
}
