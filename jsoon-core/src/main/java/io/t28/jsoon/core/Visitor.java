package io.t28.jsoon.core;

import io.t28.jsoon.core.element.ArrayElement;
import io.t28.jsoon.core.element.NullElement;
import io.t28.jsoon.core.element.ObjectElement;
import io.t28.jsoon.core.element.PrimitiveElement;

import javax.annotation.Nonnull;

public interface Visitor {
    void visit(@Nonnull PrimitiveElement element);

    void visit(@Nonnull ArrayElement element);

    void visit(@Nonnull ObjectElement element);

    void visit(@Nonnull NullElement element);
}
