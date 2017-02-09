package io.t28.pojojson.core.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.pojojson.core.naming.NamingStrategy;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public abstract class ClassBuilder {
    protected final String name;
    protected final NamingStrategy fieldNameStrategy;
    protected final NamingStrategy methodNameStrategy;
    protected final NamingStrategy parameterNameStrategy;
    private final Set<Modifier> modifiers;
    private final Map<String, TypeName> properties;
    private final List<TypeSpec> innerTypes;

    protected ClassBuilder(@Nonnull String name,
                           @Nonnull NamingStrategy fieldNameStrategy,
                           @Nonnull NamingStrategy methodNameStrategy,
                           @Nonnull NamingStrategy parameterNameStrategy) {
        this.name = name;
        this.fieldNameStrategy = fieldNameStrategy;
        this.methodNameStrategy = methodNameStrategy;
        this.parameterNameStrategy = parameterNameStrategy;
        this.modifiers = new HashSet<>();
        this.properties = new HashMap<>();
        this.innerTypes = new ArrayList<>();
    }

    @Nonnull
    public ClassBuilder addModifiers(@Nonnull Modifier... modifiers) {
        Collections.addAll(this.modifiers, modifiers);
        return this;
    }

    @Nonnull
    public ClassBuilder addProperty(@Nonnull String name, @Nonnull TypeName type) {
        properties.put(name, type);
        return this;
    }

    @Nonnull
    public ClassBuilder addInnerType(@Nonnull TypeSpec innerType) {
        innerTypes.add(innerType);
        return this;
    }

    @Nonnull
    public TypeSpec build() {
        final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(name);
        buildAnnotations().forEach(classBuilder::addAnnotation);
        buildModifiers().forEach(classBuilder::addModifiers);
        buildSuperClass().ifPresent(classBuilder::superclass);
        buildInterfaces().forEach(classBuilder::addSuperinterface);
        buildFields().forEach(classBuilder::addField);
        buildMethods().forEach(classBuilder::addMethod);
        buildInnerTypes().forEach(classBuilder::addType);
        return classBuilder.build();
    }

    @Nonnull
    protected Map<String, TypeName> getProperties() {
        return ImmutableMap.copyOf(properties);
    }

    @Nonnull
    protected List<AnnotationSpec> buildAnnotations() {
        return Collections.emptyList();
    }

    @Nonnull
    protected Set<Modifier> buildModifiers() {
        return ImmutableSet.copyOf(modifiers);
    }

    @Nonnull
    protected Optional<ClassName> buildSuperClass() {
        return Optional.empty();
    }

    @Nonnull
    protected List<TypeName> buildInterfaces() {
        return Collections.emptyList();
    }

    @Nonnull
    protected List<FieldSpec> buildFields() {
        return Collections.emptyList();
    }

    @Nonnull
    protected List<MethodSpec> buildMethods() {
        return Collections.emptyList();
    }

    @Nonnull
    protected List<TypeSpec> buildInnerTypes() {
        return ImmutableList.copyOf(innerTypes);
    }
}
