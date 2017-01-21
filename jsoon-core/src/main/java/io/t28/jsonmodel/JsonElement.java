package io.t28.jsonmodel;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.stream.Stream;

public abstract class JsonElement {
    protected final String name;
    protected final JsonNode node;

    public JsonElement(@Nonnull String name, @Nonnull JsonNode node) {
        this.name = name;
        this.node = node;
    }

    @Nonnull
    @CheckReturnValue
    public static JsonElement create(@Nonnull JsonNode node) {
        return create("", node);
    }

    @Nonnull
    @CheckReturnValue
    public static JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
        return Stream.of(ValueType.values())
                .filter(type -> type.isAcceptable(node))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported node(" + node + ") type was given"))
                .create(name, node);
    }

    @Nonnull
    @CheckReturnValue
    public String getName() {
        return name;
    }

    @CheckReturnValue
    public boolean isNull() {
        return false;
    }

    @CheckReturnValue
    public boolean isBoolean() {
        return this instanceof JsonBoolean;
    }

    @CheckReturnValue
    public boolean isNumber() {
        return this instanceof JsonNumber;
    }

    @CheckReturnValue
    public boolean isString() {
        return this instanceof JsonString;
    }

    @CheckReturnValue
    public boolean isArray() {
        return this instanceof JsonArray;
    }

    @CheckReturnValue
    public boolean isObject() {
        return this instanceof JsonObject;
    }

    public abstract void accept(@Nonnull Visitor visitor);

    enum ValueType {
        BOOLEAN {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isBoolean();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonBoolean(name, node);
            }
        },
        INT {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isInt();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonNumber(name, node, int.class);
            }
        },
        LONG {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isLong();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonNumber(name, node, long.class);
            }
        },
        FLOAT {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isFloat();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonNumber(name, node, float.class);
            }
        },
        DOUBLE {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isDouble();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonNumber(name, node, double.class);
            }
        },
        STRING {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isTextual();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonString(name, node);
            }
        },
        ARRAY {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isArray();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonArray(name, node);
            }
        },
        OBJECT {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isObject();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonObject(name, node);
            }
        },
        NULL {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return node.isNull();
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return new JsonNull(name, node);
            }
        };

        abstract boolean isAcceptable(@Nonnull JsonNode node);

        @Nonnull
        abstract JsonElement create(@Nonnull String name, @Nonnull JsonNode node);
    }
}
