package io.t28.json2java.core;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.FieldSpecAssert;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpecAssert;
import io.t28.json2java.core.json.JsonValue;
import io.t28.json2java.core.json.JsonValueAssert;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Assertions extends org.assertj.core.api.Assertions {
    private Assertions() {
    }

    @Nonnull
    @CheckReturnValue
    public static JsonValueAssert assertThat(@Nullable JsonValue actual) {
        return new JsonValueAssert(actual);
    }

    @Nonnull
    @CheckReturnValue
    public static FieldSpecAssert assertThat(@Nullable FieldSpec actual) {
        return new FieldSpecAssert(actual);
    }

    @Nonnull
    @CheckReturnValue
    public static MethodSpecAssert assertThat(@Nullable MethodSpec actual) {
        return new MethodSpecAssert(actual);
    }
}
