package io.t28.jsonmodel;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Nonnull;

public class JsonString extends JsonElement {
    public JsonString(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }
}
