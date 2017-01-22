package io.t28.jsoon.core.json;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.stream.Stream;
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
    @CheckReturnValue
    public Stream<JsonElement> stream() {
        final Iterable<JsonNode> children = getNode()::elements;
        return StreamSupport.stream(children.spliterator(), false)
                .map(child -> JsonElement.wrap(getName(), child));
    }
}
