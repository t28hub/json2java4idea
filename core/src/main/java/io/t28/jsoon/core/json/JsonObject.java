package io.t28.jsoon.core.json;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonObject extends JsonElement {
    public JsonObject(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    @CheckReturnValue
    public Stream<JsonElement> stream() {
        final Iterable<Map.Entry<String, JsonNode>> fields = getNode()::fields;
        return StreamSupport.stream(fields.spliterator(), false)
                .map(field -> {
                    final String name = field.getKey();
                    final JsonNode node = field.getValue();
                    return JsonElement.wrap(name, node);
                });
    }
}
