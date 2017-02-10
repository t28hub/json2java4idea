package io.t28.pojojson.core;

import io.t28.pojojson.core.builder.ClassBuilder;
import io.t28.pojojson.core.builder.GsonClassBuilder;
import io.t28.pojojson.core.builder.JacksonClassBuilder;
import io.t28.pojojson.core.builder.ModelClassBuilder;
import io.t28.pojojson.core.naming.NamingStrategy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

public enum Style {
    NONE {
        @Nonnull
        @Override
        public ClassBuilder newBuilder(@Nonnull String className,
                                       @Nonnull NamingStrategy fieldNameStrategy,
                                       @Nonnull NamingStrategy methodNameStrategy,
                                       @Nonnull NamingStrategy parameterNameStrategy) {
            return new ModelClassBuilder(
                    className,
                    fieldNameStrategy,
                    methodNameStrategy,
                    parameterNameStrategy
            );
        }
    },
    GSON {
        @Nonnull
        @Override
        public ClassBuilder newBuilder(@Nonnull String className,
                                       @Nonnull NamingStrategy fieldNameStrategy,
                                       @Nonnull NamingStrategy methodNameStrategy,
                                       @Nonnull NamingStrategy parameterNameStrategy) {
            return new GsonClassBuilder(
                    className,
                    fieldNameStrategy,
                    methodNameStrategy,
                    parameterNameStrategy
            );
        }
    },
    JACKSON {
        @Nonnull
        @Override
        public ClassBuilder newBuilder(@Nonnull String className,
                                       @Nonnull NamingStrategy fieldNameStrategy,
                                       @Nonnull NamingStrategy methodNameStrategy,
                                       @Nonnull NamingStrategy parameterNameStrategy) {
            return new JacksonClassBuilder(
                    className,
                    fieldNameStrategy,
                    methodNameStrategy,
                    parameterNameStrategy
            );
        }
    };

    @Nonnull
    @CheckReturnValue
    public abstract ClassBuilder newBuilder(@Nonnull String className,
                                            @Nonnull NamingStrategy fieldNameStrategy,
                                            @Nonnull NamingStrategy methodNameStrategy,
                                            @Nonnull NamingStrategy parameterNameStrategy);

    @Nonnull
    @CheckReturnValue
    public static Optional<Style> fromName(@Nonnull String name) {
        return Stream.of(values())
                .filter(style -> style.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
