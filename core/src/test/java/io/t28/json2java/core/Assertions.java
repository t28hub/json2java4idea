package io.t28.json2java.core;

import io.t28.json2java.core.json.JsonValue;
import io.t28.json2java.core.json.JsonValueAssert;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Assertions extends org.assertj.core.api.Assertions {
    @Nonnull
    @CheckReturnValue
    public static JsonValueAssert assertThat(@Nullable JsonValue actual) {
        return new JsonValueAssert(actual);
    }
}
