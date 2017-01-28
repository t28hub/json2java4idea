package io.t28.model.json.core.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.naming.NamingRule;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

class ModelClassBuilder extends ClassBuilder {
    ModelClassBuilder(@Nonnull String name, @Nonnull NamingRule fieldNameRule, @Nonnull NamingRule methodNameRule) {
        super(name, fieldNameRule, methodNameRule);
    }

    @Nonnull
    @Override
    public TypeSpec build() {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(getName());
        getModifiers().forEach(classBuilder::addModifiers);
        getProperties().forEach(property -> {
                    final String fieldName = property.format(getFieldNameRule());
                    final TypeName type = property.type();
                    classBuilder.addField(FieldSpec.builder(type, fieldName)
                            .addModifiers(Modifier.PUBLIC)
                            .build());
                });
        classBuilder.addTypes(getInnerClasses());
        return classBuilder.build();
    }
}
