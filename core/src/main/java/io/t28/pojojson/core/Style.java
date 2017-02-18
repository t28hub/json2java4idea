package io.t28.pojojson.core;

import io.t28.pojojson.core.builder.*;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

public enum Style {
    NONE {
        @Nonnull
        @Override
        public ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                      @Nonnull NamePolicy methodNameStrategy,
                                      @Nonnull NamePolicy parameterNameStrategy) {
            return new ModelClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
        }
    },
    GSON {
        @Nonnull
        @Override
        public ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                      @Nonnull NamePolicy methodNameStrategy,
                                      @Nonnull NamePolicy parameterNameStrategy) {
            return new GsonClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
        }
    },
    JACKSON {
        @Nonnull
        @Override
        public ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                      @Nonnull NamePolicy methodNameStrategy,
                                      @Nonnull NamePolicy parameterNameStrategy) {
            return new JacksonClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
        }
    },
    MOSHI {
        @Nonnull
        @Override
        public ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                      @Nonnull NamePolicy methodNameStrategy,
                                      @Nonnull NamePolicy parameterNameStrategy) {
            return new MoshiClassBuilder(fieldNameStrategy, methodNameStrategy, parameterNameStrategy);
        }
    };

    @Nonnull
    @CheckReturnValue
    public abstract ClassBuilder toBuilder(@Nonnull NamePolicy fieldNameStrategy,
                                           @Nonnull NamePolicy methodNameStrategy,
                                           @Nonnull NamePolicy parameterNameStrategy);

    @Nonnull
    @CheckReturnValue
    public static Optional<Style> fromName(@Nonnull String name) {
        return Stream.of(values())
                .filter(style -> style.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
