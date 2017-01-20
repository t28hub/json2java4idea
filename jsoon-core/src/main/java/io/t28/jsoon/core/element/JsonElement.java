package io.t28.jsoon.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import io.t28.jsoon.core.Visitor;

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
    public static JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
        final ValueType found = Stream.of(ValueType.values())
                .filter(type -> type.isAcceptable(node))
                .findFirst()
                .orElse(ValueType.UNKNOWN);
        return found.create(name, node);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public JsonNode getNode() {
        return node;
    }

    public abstract boolean isNull();

    public abstract boolean isPrimitive();

    public abstract boolean isArray();

    public abstract boolean isObject();

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
                return new PrimitiveElement(name, node, boolean.class);
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
                return new PrimitiveElement(name, node, int.class);
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
                return new PrimitiveElement(name, node, long.class);
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
                return new PrimitiveElement(name, node, float.class);
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
                return new PrimitiveElement(name, node, double.class);
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
                return new PrimitiveElement(name, node, String.class);
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
                return new ArrayElement(name, node);
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
                return new ObjectElement(name, node);
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
                return new NullElement(name, node);
            }
        },
        UNKNOWN {
            @Override
            boolean isAcceptable(@Nonnull JsonNode node) {
                return false;
            }

            @Nonnull
            @Override
            JsonElement create(@Nonnull String name, @Nonnull JsonNode node) {
                return null;
            }
        };

        abstract boolean isAcceptable(@Nonnull JsonNode node);

        @Nonnull
        abstract JsonElement create(@Nonnull String name, @Nonnull JsonNode node);
    }
}
