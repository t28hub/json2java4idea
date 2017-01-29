package io.t28.model.json.core.builder;

import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.Context;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

class GsonClassBuilder extends ClassBuilder {
    GsonClassBuilder(@Nonnull String name, @Nonnull Context context) {
        super(name, context);
    }

    @Nonnull
    @Override
    public TypeSpec build() {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(getName());
        getModifiers().forEach(classBuilder::addModifiers);

        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        constructorBuilder.addModifiers(Modifier.PUBLIC);

        final Context context = getContext();
        final NamingStrategy fieldNameStrategy = context.fieldNameStrategy();
        final NamingStrategy methodNameStrategy = context.methodNameStrategy();
        getProperties().entrySet().forEach(property -> {
            final String name = property.getKey();
            final TypeName type = property.getValue();

            final String fieldName = fieldNameStrategy.transform(name, type);
            classBuilder.addField(FieldSpec.builder(type, fieldName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .addAnnotation(AnnotationSpec.builder(SerializedName.class)
                            .addMember("value", "$S", name)
                            .build())
                    .build());

            final String methodName = methodNameStrategy.transform(name, type);
            classBuilder.addMethod(MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(type)
                    .addStatement("return $L", fieldName)
                    .build());

            final String propertyName = context.propertyNameStrategy().transform(name, type);
            constructorBuilder.addParameter(ParameterSpec.builder(type, propertyName)
                    .build())
                    .addStatement("this.$L = $L", fieldName, propertyName);
        });

        classBuilder.addMethod(constructorBuilder.build());
        classBuilder.addTypes(getInnerClasses());
        return classBuilder.build();
    }
}
