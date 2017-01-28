package io.t28.model.json.core.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

class ModelClassBuilder extends ClassBuilder {
    ModelClassBuilder(@Nonnull String name, @Nonnull NamingStrategy fieldNameStrategy, @Nonnull NamingStrategy methodNameStrategy) {
        super(name, fieldNameStrategy, methodNameStrategy);
    }

    @Nonnull
    @Override
    public TypeSpec build() {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(getName());
        getModifiers().forEach(classBuilder::addModifiers);

        final NamingStrategy fieldNameStrategy = getFieldNameStrategy();
        getProperties().forEach(property -> {
            final String fieldName = fieldNameStrategy.apply(property.type(), property.name(), property.nameCase());
            final TypeName type = property.type();
            classBuilder.addField(FieldSpec.builder(type, fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .build());
        });

        classBuilder.addTypes(getInnerClasses());
        return classBuilder.build();
    }
}
