package io.t28.jsoon.core.naming;

import javax.annotation.Nonnull;

public interface NamingStrategy {
    @Nonnull
    String toClassName(@Nonnull String name);

    @Nonnull
    String toFieldName(@Nonnull String name);

    @Nonnull
    String toMethodName(@Nonnull String name);
}
