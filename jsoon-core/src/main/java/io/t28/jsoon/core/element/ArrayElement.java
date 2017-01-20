package io.t28.jsoon.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import io.t28.jsoon.core.Visitor;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ArrayElement extends JsonElement {
    public ArrayElement(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    public boolean isEmpty() {
        return node.size() == 0;
    }

    @Nonnull
    public Optional<JsonElement> getFirstElement() {
        if (node.size() == 0) {
            return Optional.empty();
        }

        final JsonNode firstNode = node.get(0);
        return Optional.of(JsonElement.create(name, firstNode));
    }
}
