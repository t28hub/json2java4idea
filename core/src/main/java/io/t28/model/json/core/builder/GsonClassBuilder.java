package io.t28.model.json.core.builder;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.t28.model.json.core.Context;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

class GsonClassBuilder extends ClassBuilder {
    GsonClassBuilder(@Nonnull String name, @Nonnull Context context) {
        super(name, context);
    }

    @Nonnull
    @Override
    protected List<FieldSpec> getFields() {
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
    protected List<MethodSpec> getMethods() {
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        final Context context = getContext();
        final NamingStrategy fieldNameStrategy = context.fieldNameStrategy();
        final NamingStrategy methodNameStrategy = context.methodNameStrategy();
        final NamingStrategy propertyNameStrategy = context.propertyNameStrategy();
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

            final String propertyName = propertyNameStrategy.transform(name, type);
            constructorBuilder.addParameter(ParameterSpec.builder(type, propertyName)
                    .build())
                    .addStatement("this.$L = $L", fieldName, propertyName);
        });
        builder.add(constructorBuilder.build());
        return builder.build();
    }
}
