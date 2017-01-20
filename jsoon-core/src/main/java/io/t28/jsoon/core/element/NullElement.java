package io.t28.jsoon.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import io.t28.jsoon.core.Visitor;

import javax.annotation.Nonnull;

public class NullElement extends JsonElement {
    public NullElement(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }
}
