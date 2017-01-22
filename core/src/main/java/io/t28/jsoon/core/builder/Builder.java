package io.t28.jsoon.core.builder;

import javax.annotation.Nonnull;

public interface Builder<T> {
    @Nonnull
    T build();
}
