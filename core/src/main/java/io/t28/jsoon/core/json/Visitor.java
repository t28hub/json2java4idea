package io.t28.jsoon.core.json;

import javax.annotation.Nonnull;

public interface Visitor {
    void visit(@Nonnull JsonNull element);

    void visit(@Nonnull JsonPrimitive element);

    void visit(@Nonnull JsonArray element);

    void visit(@Nonnull JsonObject element);
}
