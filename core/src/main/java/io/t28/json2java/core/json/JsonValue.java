package io.t28.json2java.core.json;

import com.google.common.base.MoreObjects;
import com.squareup.javapoet.TypeName;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public abstract class JsonValue {
    @Nonnull
    @CheckReturnValue
    public static JsonValue wrap(@Nullable Object value) {
        return Stream.of(ValueType.values())
                .filter(type -> type.isAcceptable(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported type of value(" + value + ")"))
                .wrap(value);
    }

    @Nonnull
    public abstract TypeName getType();

    public abstract Object getValue();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", getType())
                .add("value", getValue())
                .toString();
    }

    @CheckReturnValue
    public boolean isNull() {
        return this instanceof JsonNull;
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

    @Nonnull
    @CheckReturnValue
    public JsonNull asNull() {
        if (isNull()) {
            return (JsonNull) this;
        }
        throw new IllegalStateException("This value is not a null");
    }

    @Nonnull
    @CheckReturnValue
    public JsonBoolean asBoolean() {
        if (isBoolean()) {
            return (JsonBoolean) this;
        }
        throw new IllegalStateException("This value is not a boolean");
    }

    @Nonnull
    @CheckReturnValue
    public JsonNumber asNumber() {
        if (isNumber()) {
            return (JsonNumber) this;
        }
        throw new IllegalStateException("This value is not a number");
    }

    @Nonnull
    @CheckReturnValue
    public JsonString asString() {
        if (isString()) {
            return (JsonString) this;
        }
        throw new IllegalStateException("This value is not a string");
    }

    @Nonnull
    @CheckReturnValue
    public JsonArray asArray() {
        if (isArray()) {
            return (JsonArray) this;
        }
        throw new IllegalStateException("This value is not an array");
    }

    @Nonnull
    @CheckReturnValue
    public JsonObject asObject() {
        if (isObject()) {
            return (JsonObject) this;
        }
        throw new IllegalStateException("This value is not an object");
    }
}
