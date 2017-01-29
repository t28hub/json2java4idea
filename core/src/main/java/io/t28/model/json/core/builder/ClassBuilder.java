package io.t28.model.json.core.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.Context;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public abstract class ClassBuilder {
    private final String name;
    private final Context context;
    private final Set<Modifier> modifiers;
    private final Map<String, TypeName> properties;
    private final List<TypeSpec> innerClasses;

    protected ClassBuilder(@Nonnull String name, @Nonnull Context context) {
        this.name = name;
        this.context = context;
        this.modifiers = new HashSet<>();
        this.properties = new HashMap<>();
        this.innerClasses = new ArrayList<>();
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
    public ClassBuilder addInnerClass(@Nonnull TypeSpec innerClass) {
        innerClasses.add(innerClass);
        return this;
    }

    @Nonnull
    public abstract TypeSpec build();

    @Nonnull
    protected String getName() {
        return name;
    }

    @Nonnull
    protected Context getContext() {
        return context;
    }

    @Nonnull
    protected Set<Modifier> getModifiers() {
        return ImmutableSet.copyOf(modifiers);
    }

    @Nonnull
    protected Map<String, TypeName> getProperties() {
        return ImmutableMap.copyOf(properties);
    }

    @Nonnull
    protected List<TypeSpec> getInnerClasses() {
        return ImmutableList.copyOf(innerClasses);
    }
}
