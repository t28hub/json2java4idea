package io.t28.model.json.core.builder;

import io.t28.model.json.core.Context;

import javax.annotation.Nonnull;

public enum BuilderType {
    MODEL {
        @Nonnull
        @Override
        public ClassBuilder create(@Nonnull String className, @Nonnull Context context) {
            return new ModelClassBuilder(className, context);
        }
    };

    @Nonnull
    public abstract ClassBuilder create(@Nonnull String className, @Nonnull Context context);
}
