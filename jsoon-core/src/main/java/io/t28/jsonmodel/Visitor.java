package io.t28.jsonmodel;

import javax.annotation.Nonnull;

public interface Visitor {
    void visit(@Nonnull JsonNull element);

    void visit(@Nonnull JsonBoolean element);

    void visit(@Nonnull JsonNumber element);

    void visit(@Nonnull JsonString element);

    void visit(@Nonnull JsonArray element);

    void visit(@Nonnull JsonObject element);
}
