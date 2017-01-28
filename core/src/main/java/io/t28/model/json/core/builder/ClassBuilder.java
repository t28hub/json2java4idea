package io.t28.model.json.core.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingRule;
import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Value.Enclosing
public abstract class ClassBuilder {
    private final String name;
    private final NamingRule fieldNameRule;
    private final NamingRule methodNameRule;
    private final Set<Modifier> modifiers;
    private final List<Property> properties;
    private final List<TypeSpec> innerClasses;

    protected ClassBuilder(@Nonnull String name, @Nonnull NamingRule fieldNameRule, @Nonnull NamingRule methodNameRule) {
        this.name = name;
        this.fieldNameRule = fieldNameRule;
        this.methodNameRule = methodNameRule;
        this.modifiers = new HashSet<>();
        this.properties = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
    }

    @Nonnull
    public ClassBuilder addModifiers(@Nonnull Modifier... modifiers) {
        Collections.addAll(this.modifiers, modifiers);
        return this;
    }

    @Nonnull
    public ClassBuilder addProperty(@Nonnull Property property) {
        properties.add(property);
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
    protected NamingRule getFieldNameRule() {
        return fieldNameRule;
    }

    @Nonnull
    protected NamingRule getMethodNameRule() {
        return methodNameRule;
    }

    @Nonnull
    protected Set<Modifier> getModifiers() {
        return ImmutableSet.copyOf(modifiers);
    }

    @Nonnull
    protected List<Property> getProperties() {
        return ImmutableList.copyOf(properties);
    }

    @Nonnull
    protected List<TypeSpec> getInnerClasses() {
        return ImmutableList.copyOf(innerClasses);
    }

    @Value.Immutable
    @SuppressWarnings("NullableProblems")
    public interface Property {
        @Nonnull
        NamingCase nameCase();

        @Nonnull
        String name();

        @Nonnull
        TypeName type();

        @Nonnull
        default String format(@Nonnull NamingRule rule) {
            return rule.format(nameCase(), name());
        }

        @Nonnull
        static ImmutableClassBuilder.Property.Builder builder() {
            return ImmutableClassBuilder.Property.builder();
        }
    }

    public enum Type {
        MODEL {
            @Nonnull
            @Override
            public ClassBuilder create(@Nonnull String className, @Nonnull NamingRule fieldNameRule, @Nonnull NamingRule methodNameRule) {
                return new ModelClassBuilder(className, fieldNameRule, methodNameRule);
            }
        };

        @Nonnull
        public abstract ClassBuilder create(@Nonnull String className, @Nonnull NamingRule fieldNameRule, @Nonnull NamingRule methodNameRule);
    }
}
