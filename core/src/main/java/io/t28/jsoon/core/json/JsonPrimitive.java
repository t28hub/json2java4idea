package io.t28.jsoon.core.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.TypeName;

import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public class JsonPrimitive extends JsonElement {
    private final Type type;

    public JsonPrimitive(@Nonnull String name, @Nonnull JsonNode node, @Nonnull Type type) {
        super(name, node);
        this.type = type;
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    public TypeName getTypeName() {
        return TypeName.get(type);
    }

    @Nonnull
    public TypeName getBoxedTypeName() {
        return getTypeName().box();
    }
}
