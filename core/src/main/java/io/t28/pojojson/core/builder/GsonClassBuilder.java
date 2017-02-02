package io.t28.pojojson.core.builder;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.Context;
import io.t28.pojojson.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class GsonClassBuilder extends ClassBuilder {
    public GsonClassBuilder(@Nonnull String name, @Nonnull Context context) {
        super(name, context);
    }

    @Nonnull
    @Override
    protected List<FieldSpec> buildFields() {
        final Context context = getContext();
        final NamingStrategy fieldNameStrategy = context.fieldNameStrategy();
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

        final Context context = getContext();
        final NamingStrategy fieldNameStrategy = context.fieldNameStrategy();
        final NamingStrategy methodNameStrategy = context.methodNameStrategy();
        final NamingStrategy parameterNameStrategy = context.parameterNameStrategy();
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
