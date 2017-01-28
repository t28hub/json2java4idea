package io.t28.model.json.core.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.Context;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

class ModelClassBuilder extends ClassBuilder {
    ModelClassBuilder(@Nonnull String name, @Nonnull Context context) {
        super(name, context);
    }

    @Nonnull
    @Override
    public TypeSpec build() {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(getName());
        getModifiers().forEach(classBuilder::addModifiers);

        final Context context = getContext();
        final NamingCase nameCase = context.nameCase();
        final NamingStrategy fieldNameStrategy = context.fieldNameStrategy();
        final NamingStrategy methodNameStrategy = context.methodNameStrategy();
        final MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        getProperties().forEach(property -> {
            final String name = property.name();
            final TypeName type = property.type();

            final String fieldName = fieldNameStrategy.transform(type, name, nameCase);
            classBuilder.addField(FieldSpec.builder(type, fieldName)
                    .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                    .build());

            final String methodName = methodNameStrategy.transform(type, name, nameCase);
            classBuilder.addMethod(MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(type)
                    .addStatement("return $L", fieldName)
                    .build());

            final String propertyName = context.propertyNameStrategy().transform(type, name, nameCase);
            constructorBuilder.addParameter(ParameterSpec.builder(type, propertyName)
                    .build())
                    .addStatement("this.$L = $L", fieldName, propertyName);

        });
        classBuilder.addMethod(constructorBuilder.build());
        classBuilder.addTypes(getInnerClasses());
        return classBuilder.build();
    }
}
