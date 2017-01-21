package io.t28.jsoon.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import io.t28.jsoon.core.Visitor;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class ObjectElement extends JsonElement {
    public ObjectElement(@Nonnull String name, @Nonnull JsonNode node) {
        super(name, node);
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    public void each(@Nonnull Consumer<JsonElement> consumer) {
        final Iterable<Map.Entry<String, JsonNode>> fields = getNode()::fields;
        StreamSupport.stream(fields.spliterator(), false)
                .map(field -> {
                    final String name = field.getKey();
                    final JsonNode node = field.getValue();
                    return JsonElement.create(name, node);
                })
                .forEach(consumer);
    }
}
