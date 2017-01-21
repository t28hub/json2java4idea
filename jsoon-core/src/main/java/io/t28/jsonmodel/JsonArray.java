package io.t28.jsonmodel;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class JsonArray extends JsonElement {
    public JsonArray(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    public Optional<JsonElement> findFirst() {
        final Iterable<JsonNode> children = node::elements;
        return StreamSupport.stream(children.spliterator(), false)
                .findFirst()
                .map(child -> JsonElement.create(name, child));
    }
}
