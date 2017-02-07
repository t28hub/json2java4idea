package io.t28.pojojson.core;

import io.t28.pojojson.core.builder.ClassBuilder;
import io.t28.pojojson.core.builder.GsonClassBuilder;
import io.t28.pojojson.core.builder.JacksonClassBuilder;
import io.t28.pojojson.core.builder.ModelClassBuilder;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Stream;

public enum ClassStyle {
    MODEL {
        @Nonnull
        @Override
        public ClassBuilder create(@Nonnull String className, @Nonnull Context context) {
            return new ModelClassBuilder(className, context);
        }
    },
    GSON {
        @Nonnull
        @Override
        public ClassBuilder create(@Nonnull String className, @Nonnull Context context) {
            return new GsonClassBuilder(className, context);
        }
    },
    JACKSON {
        @Nonnull
        @Override
        public ClassBuilder create(@Nonnull String className, @Nonnull Context context) {
            return new JacksonClassBuilder(className, context);
        }
    };

    @Nonnull
    @CheckReturnValue
    public abstract ClassBuilder create(@Nonnull String className, @Nonnull Context context);

    @Nonnull
    @CheckReturnValue
    public static Optional<ClassStyle> fromName(@Nonnull String name) {
        return Stream.of(values())
                .filter(style -> style.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
