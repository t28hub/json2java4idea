package io.t28.pojojson.core.builder;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class GsonClassBuilder extends ClassBuilder {
    public GsonClassBuilder(@Nonnull String name,
                            @Nonnull NamingStrategy fieldNameStrategy,
                            @Nonnull NamingStrategy methodNameStrategy,
                            @Nonnull NamingStrategy parameterNameStrategy) {
        super(name, fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
    }

    @Nonnull
    @Override
    protected List<FieldSpec> buildFields() {
        return getProperties().entrySet()
                .stream()
                .map(property -> {
                    final String name = property.getKey();
                    final TypeName type = property.getValue();

                    final String fieldName = fieldNameStrategy.transform(name, type);
                    return FieldSpec.builder(type, fieldName)
                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                            .addAnnotation(AnnotationSpec.builder(SerializedName.class)
                                    .addMember("value", "$S", name)
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());
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

            final String fieldName = fieldNameStrategy.transform(name, type);
            final String methodName = methodNameStrategy.transform(name, type);
            builder.add(MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(type)
                    .addStatement("return $L", fieldName)
                    .build());

            final String parameterName = parameterNameStrategy.transform(name, type);
            constructorBuilder.addParameter(ParameterSpec.builder(type, parameterName)
                    .build())
                    .addStatement("this.$L = $L", fieldName, parameterName);
        });
        builder.add(constructorBuilder.build());
        return builder.build();
    }
}
