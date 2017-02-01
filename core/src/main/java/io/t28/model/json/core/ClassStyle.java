package io.t28.model.json.core;

import io.t28.model.json.core.builder.ClassBuilder;
import io.t28.model.json.core.builder.GsonClassBuilder;
import io.t28.model.json.core.builder.JacksonClassBuilder;
import io.t28.model.json.core.builder.ModelClassBuilder;

import javax.annotation.Nonnull;

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
    public abstract ClassBuilder create(@Nonnull String className, @Nonnull Context context);
}
