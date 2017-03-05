package io.t28.json2java.core.json;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNull();
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a null");
        }
    },
    BOOLEAN {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Boolean;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonBoolean((Boolean) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a boolean");
        }
    },
    INT {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Integer;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(int.class, (Integer) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not an int");
        }
    },
    LONG {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Long;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(long.class, (Long) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a long");
        }
    },
    FLOAT {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Float;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(float.class, (Float) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a float");
        }
    },
    DOUBLE {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof Double;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(double.class, (Double) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a double");
        }
    },
    BIG_INTEGER {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof BigInteger;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(BigInteger.class, (BigInteger) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a big integer");
        }
    },
    BIG_DECIMAL {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof BigDecimal;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonNumber(BigDecimal.class, (BigDecimal) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a big decimal");
        }
    },
    STRING {
        @Override
        boolean isAcceptable(@Nullable Object value) {
            return value instanceof String;
        }

        @Nonnull
        @Override
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonString((String) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not a string");
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
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonArray((List<Object>) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not an array");
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
        JsonValue wrap(@Nullable Object value) {
            if (isAcceptable(value)) {
                return new JsonObject((Map<String, Object>) value);
            }
            throw new IllegalArgumentException("Value '" + value + "' is not an object");
        }
    };

    @CheckReturnValue
    abstract boolean isAcceptable(@Nullable Object value);

    @Nonnull
    @CheckReturnValue
    abstract JsonValue wrap(@Nullable Object value);
}
