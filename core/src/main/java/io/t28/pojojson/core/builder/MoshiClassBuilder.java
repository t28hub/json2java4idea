package io.t28.pojojson.core.builder;

import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.*;
import com.squareup.moshi.Json;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class MoshiClassBuilder extends ClassBuilder {
    public MoshiClassBuilder(@Nonnull NamePolicy fieldNamePolicy,
                             @Nonnull NamePolicy methodNamePolicy,
                             @Nonnull NamePolicy parameterNamePolicy) {
        super(fieldNamePolicy, methodNamePolicy, parameterNamePolicy);
    }

    @Nonnull
    @Override
    protected List<FieldSpec> buildFields() {
        return getProperties().entrySet().stream().map(property -> {
            final String name = property.getKey();
            final TypeName type = property.getValue();
            final String fieldName = fieldNamePolicy.convert(name, type);
            return FieldSpec.builder(type, fieldName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .addAnnotation(AnnotationSpec.builder(Json.class)
                            .addMember("name", "$S", name)
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    @Nonnull
    @Override
    protected List<MethodSpec> buildMethods() {
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        final ImmutableList.Builder<MethodSpec> builder = ImmutableList.builder();
        getProperties().entrySet().forEach(property -> {
            final String name = property.getKey();
            final TypeName type = property.getValue();
            final String fieldName = fieldNamePolicy.convert(name, type);
            final String methodName = methodNamePolicy.convert(name, type);
            builder.add(MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(type)
                    .addStatement("return $L", fieldName)
                    .build());

            final String propertyName = parameterNamePolicy.convert(name, type);
            constructorBuilder.addParameter(ParameterSpec.builder(type, propertyName)
                    .build())
                    .addStatement("this.$L = $L", fieldName, propertyName);
        });
        builder.add(constructorBuilder.build());
        return builder.build();
    }
}
