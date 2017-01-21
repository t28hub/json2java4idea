package io.t28.jsonmodel;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.lang.reflect.Type;

public class JsonNumber extends JsonElement {
    private final Type type;

    public JsonNumber(@Nonnull String name, @Nonnull JsonNode node, @Nonnull Type type) {
        super(name, node);
        this.type = type;
    }

    @Override
    public void accept(@Nonnull Visitor visitor) {
        visitor.visit(this);
    }

    @Nonnull
    @CheckReturnValue
    public TypeName getTypeName() {
        return TypeName.get(type);
    }

    @Nonnull
    @CheckReturnValue
    public TypeName getBoxedTypeName() {
        return getTypeName().box();
    }
}
