package io.t28.jsoon.core.json;

import com.fasterxml.jackson.databind.JsonNode;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

enum ValueType {
    NULL {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isNull();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonNull(name, node);
        }
    },
    BOOLEAN {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isBoolean();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonPrimitive(name, node, boolean.class);
        }
    },
    INT {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isInt();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonPrimitive(name, node, int.class);
        }
    },
    LONG {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isLong();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonPrimitive(name, node, long.class);
        }
    },
    FLOAT {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isFloat();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonPrimitive(name, node, float.class);
        }
    },
    DOUBLE {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isDouble();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonPrimitive(name, node, double.class);
        }
    },
    STRING {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isTextual();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonPrimitive(name, node, String.class);
        }
    },
    ARRAY {
        @Override
        boolean isAcceptable(@Nonnull JsonNode node) {
            return node.isArray();
        }

        @Nonnull
        @Override
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
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
        JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node) {
            return new JsonObject(name, node);
        }
    };

    @CheckReturnValue
    abstract boolean isAcceptable(@Nonnull JsonNode node);

    @Nonnull
    @CheckReturnValue
    abstract JsonElement wrap(@Nonnull String name, @Nonnull JsonNode node);
}
