package io.t28.model.json.core.builder;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModelClassBuilder implements ClassBuilder {
    private final String name;
    private final Set<Modifier> modifiers;
    private final Map<String, TypeName> properties;
    private final List<TypeSpec> enclosedClasses;

    public ModelClassBuilder(@Nonnull String name) {
        this.name = name;
        this.modifiers = new HashSet<>();
        this.properties = new HashMap<>();
        this.enclosedClasses = new ArrayList<>();
    }

    @Nonnull
    @Override
    public ClassBuilder addModifiers(@Nonnull Modifier... modifiers) {
        Collections.addAll(this.modifiers, modifiers);
        return this;
    }

    @Nonnull
    @Override
    public ClassBuilder addProperty(@Nonnull String name, @Nonnull TypeName type) {
        properties.put(name, type);
        return this;
    }

    @Nonnull
    @Override
    public ClassBuilder addEnclosedClass(@Nonnull TypeSpec enclosedClass) {
        enclosedClasses.add(enclosedClass);
        return this;
    }

    @Nonnull
    @Override
    public TypeSpec build() {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name);
        properties.entrySet().forEach(property -> {
            final String name = property.getKey();
            final TypeName type = property.getValue();
            classBuilder.addField(FieldSpec.builder(type, name)
                    .addModifiers(Modifier.PUBLIC)
                    .build());
        });
        classBuilder.addTypes(enclosedClasses);
        return classBuilder.build();
    }
}
