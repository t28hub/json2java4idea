package io.t28.model.json.core.json;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

enum ValueType {
    NULL {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value == null;
        }

        @Nonnull
        @Override
        JsonNull wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNull();
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not null");
        }
    },
    BOOLEAN {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Boolean;
        }

        @Nonnull
        @Override
        JsonBoolean wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonBoolean((Boolean) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of Boolean");
        }
    },
    INT {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Integer;
        }

        @Nonnull
        @Override
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(int.class, (Integer) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of Integer");
        }
    },
    LONG {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Long;
        }

        @Nonnull
        @Override
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(long.class, (Long) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of Long");
        }
    },
    FLOAT {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Float;
        }

        @Nonnull
        @Override
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(float.class, (Float) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of Float");
        }
    },
    DOUBLE {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Double;
        }

        @Nonnull
        @Override
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(double.class, (Double) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of Double");
        }
    },
    STRING {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof String;
        }

        @Nonnull
        @Override
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonString((String) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of String");
        }
    },
    ARRAY {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof List;
        }

        @Nonnull
        @Override
        @SuppressWarnings("unchecked")
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonArray((List<Object>) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of List");
        }
    },
    OBJECT {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Map;
        }

        @Nonnull
        @Override
        @SuppressWarnings("unchecked")
        JsonValue<?> wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonObject((Map<String, Object>) value);
            }
            throw new IllegalArgumentException("Specified value(" + value + ") is not an instance of Map");
        }
    };

    @CheckReturnValue
    abstract boolean isAcceptable(@Nullable Object value);

    @Nonnull
    @CheckReturnValue
    abstract JsonValue<?> wrap(@Nullable Object value);
}
