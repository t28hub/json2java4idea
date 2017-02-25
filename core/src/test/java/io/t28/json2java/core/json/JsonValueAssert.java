package io.t28.json2java.core.json;

import com.squareup.javapoet.TypeName;
import org.assertj.core.api.AbstractAssert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonValueAssert extends AbstractAssert<JsonValueAssert, JsonValue> {
    public JsonValueAssert(@Nullable JsonValue actual) {
        super(actual, JsonValueAssert.class);
    }

    @Nonnull
    public JsonValueAssert hasType(@Nonnull TypeName expected) {
        isNotNull();

        final TypeName actual = this.actual.getType();
        assertThat(actual)
                .overridingErrorMessage("Expected type to be <%s> but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public JsonValueAssert hasValue(@Nullable Object expected) {
        isNotNull();

        final Object actual = this.actual.getValue();
        assertThat(actual)
                .overridingErrorMessage("Expected value to be <%s> but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public JsonValueAssert isJsonNull() {
        isNotNull();

        assertThat(actual.isNull())
                .overridingErrorMessage(
                        "Expected type to be <%s> but was <%s>",
                        JsonNull.class.getSimpleName(),
                        actual.getClass().getSimpleName()
                )
                .isTrue();

        return this;
    }

    @Nonnull
    public JsonValueAssert isJsonBoolean() {
        isNotNull();

        assertThat(actual.isBoolean())
                .overridingErrorMessage(
                        "Expected type to be <%s> but was <%s>",
                        JsonBoolean.class.getSimpleName(),
                        actual.getClass().getSimpleName()
                )
                .isTrue();

        return this;
    }

    @Nonnull
    public JsonValueAssert isJsonNumber() {
        isNotNull();

        assertThat(actual.isNumber())
                .overridingErrorMessage(
                        "Expected type to be <%s> but was <%s>",
                        JsonNumber.class.getSimpleName(),
                        actual.getClass().getSimpleName()
                )
                .isTrue();

        return this;
    }

    @Nonnull
    public JsonValueAssert isJsonString() {
        isNotNull();

        assertThat(actual.isString())
                .overridingErrorMessage(
                        "Expected type to be <%s> but was <%s>",
                        JsonString.class.getSimpleName(),
                        actual.getClass().getSimpleName()
                )
                .isTrue();

        return this;
    }

    @Nonnull
    public JsonValueAssert isJsonArray() {
        isNotNull();

        assertThat(actual.isArray())
                .overridingErrorMessage(
                        "Expected type to be <%s> but was <%s>",
                        JsonArray.class.getSimpleName(),
                        actual.getClass().getSimpleName()
                )
                .isTrue();

        return this;
    }

    @Nonnull
    public JsonValueAssert isJsonObject() {
        isNotNull();

        assertThat(actual.isObject())
                .overridingErrorMessage(
                        "Expected type to be <%s> but was <%s>",
                        JsonObject.class.getSimpleName(),
                        actual.getClass().getSimpleName()
                )
                .isTrue();

        return this;
    }
}
