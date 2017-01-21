package io.t28.jsonmodel;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class JsonObject extends JsonElement {
    public JsonObject(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    public void forEach(@Nonnull Consumer<JsonElement> action) {
        final Iterable<Map.Entry<String, JsonNode>> fields = node::fields;
        StreamSupport.stream(fields.spliterator(), false)
                .map(field -> {
                    final String name = field.getKey();
                    final JsonNode node = field.getValue();
                    return JsonElement.create(name, node);
                })
                .forEach(action);
    }
}
