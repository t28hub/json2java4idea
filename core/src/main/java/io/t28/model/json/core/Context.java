package io.t28.model.json.core;

import io.t28.model.json.core.builder.BuilderType;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;
import io.t28.model.json.core.naming.defaults.ClassNameStrategy;
import io.t28.model.json.core.naming.defaults.FieldNameStrategy;
import io.t28.model.json.core.naming.defaults.MethodNameStrategy;
import io.t28.model.json.core.naming.defaults.PropertyNameStrategy;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public interface Context {
    String DEFAULT_INDENT = "    ";

    @Nonnull
    @Value.Default
    default String indent() {
        return DEFAULT_INDENT;
    }

    @Nonnull
    default BuilderType builderType() {
        return BuilderType.MODEL;
    }

    @Nonnull
    @Value.Default
    default NamingCase nameCase() {
        return NamingCase.LOWER_CAMEL_CASE;
    }

    @Nonnull
    @Value.Default
    default NamingStrategy classNameStrategy() {
        return new ClassNameStrategy(nameCase());
    }

    @Nonnull
    @Value.Default
    default NamingStrategy fieldNameStrategy() {
        return new FieldNameStrategy(nameCase());
    }

    @Nonnull
    @Value.Default
    default NamingStrategy methodNameStrategy() {
        return new MethodNameStrategy(nameCase());
    }

    @Nonnull
    @Value.Default
    default NamingStrategy propertyNameStrategy() {
        return new PropertyNameStrategy(nameCase());
    }

    @Nonnull
    static ImmutableContext.Builder builder() {
        return ImmutableContext.builder();
    }

    @Nonnull
    static Context copyOf(@Nonnull Context context) {
        return ImmutableContext.copyOf(context);
    }
}
