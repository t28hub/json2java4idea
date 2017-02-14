package io.t28.pojojson.core.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.t28.pojojson.core.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonValueTest {
    @Test
    public void wrapShouldReturnJsonNullWhenNull() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(null);

        // verify
        assertThat(actual)
                .isJsonNull()
                .hasType(TypeName.OBJECT)
                .hasValue(null);
    }

    @Test
    public void wrapShouldReturnJsonBooleanWhenBoolean() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(true);

        // verify
        assertThat(actual)
                .isJsonBoolean()
                .hasType(TypeName.BOOLEAN)
                .hasValue(true);
    }

    @Test
    public void wrapShouldReturnJsonNumberWhenInt() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(1024);

        // verify
        assertThat(actual)
                .isJsonNumber()
                .hasType(TypeName.INT)
                .hasValue(1024);
    }

    @Test
    public void wrapShouldReturnJsonNumberWhenLong() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(1024L);

        // verify
        assertThat(actual)
                .isJsonNumber()
                .hasType(TypeName.LONG)
                .hasValue(1024L);
    }

    @Test
    public void wrapShouldReturnJsonNumberWhenFloat() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(10.24f);

        // verify
        assertThat(actual)
                .isJsonNumber()
                .hasType(TypeName.FLOAT)
                .hasValue(10.24f);
    }

    @Test
    public void wrapShouldReturnJsonNumberWhenDouble() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(10.24d);

        // verify
        assertThat(actual)
                .isJsonNumber()
                .hasType(TypeName.DOUBLE)
                .hasValue(10.24d);
    }

    @Test
    public void wrapShouldReturnJsonNumberWhenBigInteger() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(BigInteger.ONE);

        // verify
        assertThat(actual)
                .isJsonNumber()
                .hasType(TypeName.get(BigInteger.class))
                .hasValue(BigInteger.ONE);
    }

    @Test
    public void wrapShouldReturnJsonNumberWhenBigDecimal() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap(BigDecimal.ONE);

        // verify
        assertThat(actual)
                .isJsonNumber()
                .hasType(TypeName.get(BigDecimal.class))
                .hasValue(BigDecimal.ONE);
    }

    @Test
    public void wrapShouldReturnJsonStringWhenString() throws Exception {
        // exercise
        final JsonValue actual = JsonValue.wrap("text");

        // verify
        assertThat(actual)
                .isJsonString()
                .hasType(TypeName.get(String.class))
                .hasValue("text");
    }

    @Test
    public void wrapShouldReturnJsonArrayWhenList() throws Exception {
        // setup
        final List<String> value = ImmutableList.of("foo", "bar", "baz");

        // exercise
        final JsonValue actual = JsonValue.wrap(value);

        // verify
        assertThat(actual)
                .isJsonArray()
                .hasType(ParameterizedTypeName.get(List.class, Object.class))
                .hasValue(value);
    }

    @Test
    public void wrapShouldReturnJsonObjectWhenMap() throws Exception {
        // setup
        final Map<String, String> value = ImmutableMap.of("foo", "bar", "baz", "qux");

        // exercise
        final JsonValue actual = JsonValue.wrap(value);

        // verify
        assertThat(actual)
                .isJsonObject()
                .hasType(ParameterizedTypeName.get(Map.class, String.class, Object.class))
                .hasValue(value);
    }

    @Test
    public void wrapShouldThrowExceptionWhenUnsupportedType() throws Exception {
        assertThatThrownBy(() -> JsonValue.wrap(Collections.emptySet()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}