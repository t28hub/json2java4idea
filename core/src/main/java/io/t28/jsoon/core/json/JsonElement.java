package io.t28.jsoon.core.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.stream.Stream;

public abstract class JsonElement {
    private static final String NO_NAME = "";

    private final String name;
    private final JsonNode node;

    public JsonElement(@Nonnull String name, @Nonnull JsonNode node) {
        this.name = name;
        this.node = node;
    }

    @Nonnull
    @CheckReturnValue
    public static JsonElement wrap(@Nonnull JsonNode node) {
        return wrap(NO_NAME, node);
    }

    @Nonnull
    @CheckReturnValue
    public static JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
        return Stream.of(ValueType.values())
                .filter(type -> type.isAcceptable(node))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported value type(" + node.getNodeType() + ")"))
                .wrap(name, node);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("node", node)
                .toString();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @CheckReturnValue
    public boolean isNull() {
        return this instanceof JsonNull;
    }

    @CheckReturnValue
    public boolean isPrimitive() {
        return this instanceof JsonPrimitive;
    }

    @CheckReturnValue
    public boolean isArray() {
        return this instanceof JsonArray;
    }

    @CheckReturnValue
    public boolean isObject() {
        return this instanceof JsonObject;
    }

    @Nonnull
    @CheckReturnValue
    public JsonNull asNull() {
        if (!isNull()) {
            throw new IllegalArgumentException("This is not a null");
        }
        return (JsonNull) this;
    }

    @Nonnull
    @CheckReturnValue
    public JsonPrimitive asPrimitive() {
        if (!isPrimitive()) {
            throw new IllegalArgumentException("This is not a primitive");
        }
        return (JsonPrimitive) this;
    }

    @Nonnull
    @CheckReturnValue
    public JsonArray asArray() {
        if (!isArray()) {
            throw new IllegalArgumentException("This is not an array");
        }
        return (JsonArray) this;
    }

    @Nonnull
    @CheckReturnValue
    public JsonObject asObject() {
        if (!isObject()) {
            throw new IllegalArgumentException("This is not an object");
        }
        return (JsonObject) this;
    }

    public abstract void accept(@Nonnull Visitor visitor);

    @Nonnull
    protected JsonNode getNode() {
        return node;
    }
}
