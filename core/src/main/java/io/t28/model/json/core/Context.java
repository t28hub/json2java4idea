package io.t28.model.json.core;

import io.t28.model.json.core.builder.BuilderType;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public interface Context {
    @Nonnull
    BuilderType builderType();

    @Nonnull
    NamingCase nameCase();

    @Nonnull
    NamingStrategy classNameStrategy();

    @Nonnull
    NamingStrategy fieldNameStrategy();

    @Nonnull
    NamingStrategy methodNameStrategy();

    @Nonnull
    NamingStrategy propertyNameStrategy();

    @Nonnull
    static ImmutableContext.Builder builder() {
        return ImmutableContext.builder();
    }
}
