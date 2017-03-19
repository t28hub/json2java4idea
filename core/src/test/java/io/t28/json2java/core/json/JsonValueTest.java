/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.json2java.core.json;

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

import static io.t28.json2java.core.Assertions.assertThat;
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

    @Test
    public void toStringShouldReturnStringRepresentation() throws Exception {
        // exercise
        final String actual = JsonValue.wrap(null).toString();

        // verify
        assertThat(actual).isEqualTo("JsonNull{type=java.lang.Object, value=null}");
    }

    @Test
    public void isNullShouldReturnTrueWhenValueIsNull() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(null).isNull();

        // verify
        assertThat(actual).isTrue();
    }

    @Test
    public void isNullShouldReturnFalseWhenValueIsNotNull() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap("null").isNull();

        // verify
        assertThat(actual).isFalse();
    }

    @Test
    public void isBooleanShouldReturnTrueWhenValueIsBoolean() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(true).isBoolean();

        // verify
        assertThat(actual).isTrue();
    }

    @Test
    public void isBooleanShouldReturnFalseWhenValueIsNotBoolean() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap("true").isBoolean();

        // verify
        assertThat(actual).isFalse();
    }

    @Test
    public void isNumberShouldReturnTrueWhenValueIsNumber() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(42).isNumber();

        // verify
        assertThat(actual).isTrue();
    }

    @Test
    public void isNumberShouldReturnFalseWhenValueIsNotNumber() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap("42").isNumber();

        // verify
        assertThat(actual).isFalse();
    }

    @Test
    public void isStringShouldReturnTrueWhenValueIsString() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap("string").isString();

        // verify
        assertThat(actual).isTrue();
    }

    @Test
    public void isStringShouldReturnFalseWhenValueIsNotString() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(null).isString();

        // verify
        assertThat(actual).isFalse();
    }

    @Test
    public void isArrayShouldReturnTrueWhenValueIsList() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(Collections.emptyList()).isArray();

        // verify
        assertThat(actual).isTrue();
    }

    @Test
    public void isArrayShouldReturnFalseWhenValueIsNotList() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(null).isArray();

        // verify
        assertThat(actual).isFalse();
    }

    @Test
    public void isObjectShouldReturnTrueWhenValueIsMap() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(Collections.emptyMap()).isObject();

        // verify
        assertThat(actual).isTrue();
    }

    @Test
    public void isObjectShouldReturnFalseWhenValueIsNotMap() throws Exception {
        // exercise
        final boolean actual = JsonValue.wrap(null).isObject();

        // verify
        assertThat(actual).isFalse();
    }

    @Test
    public void asNullShouldReturnJsonNull() throws Exception {
        // exercise
        final JsonNull actual = JsonValue.wrap(null).asNull();

        // verify
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(JsonNull.class);
    }

    @Test
    public void asNullShouldThrowExceptionWhenValueIsNotNull() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> JsonValue.wrap("null").asNull())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This value is not a null");
    }

    @Test
    public void asBooleanShouldReturnJsonBoolean() throws Exception {
        // exercise
        final JsonBoolean actual = JsonValue.wrap(true).asBoolean();

        // verify
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(JsonBoolean.class);
    }

    @Test
    public void asBooleanShouldThrowExceptionWhenValueIsNotBoolean() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> JsonValue.wrap("true").asBoolean())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This value is not a boolean");
    }

    @Test
    public void asNumberShouldReturnJsonNumber() throws Exception {
        // exercise
        final JsonNumber actual = JsonValue.wrap(42).asNumber();

        // verify
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(JsonNumber.class);
    }

    @Test
    public void asNumberShouldThrowExceptionWhenValueIsNotNumber() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> JsonValue.wrap("42").asNumber())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This value is not a number");
    }

    @Test
    public void asStringShouldReturnJsonString() throws Exception {
        // exercise
        final JsonString actual = JsonValue.wrap("string").asString();

        // verify
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(JsonString.class);
    }

    @Test
    public void asStringShouldThrowExceptionWhenValueIsNotString() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> JsonValue.wrap(null).asString())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This value is not a string");
    }

    @Test
    public void asArrayShouldReturnJsonArray() throws Exception {
        // exercise
        final JsonArray actual = JsonValue.wrap(Collections.emptyList()).asArray();

        // verify
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(JsonArray.class);
    }

    @Test
    public void asArrayShouldThrowExceptionWhenValueIsNotArray() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> JsonValue.wrap(null).asArray())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This value is not an array");
    }

    @Test
    public void asObjectShouldReturnJsonObject() throws Exception {
        // exercise
        final JsonObject actual = JsonValue.wrap(Collections.emptyMap()).asObject();

        // verify
        assertThat(actual)
                .isNotNull()
                .isInstanceOf(JsonObject.class);
    }

    @Test
    public void asObjectShouldThrowExceptionWhenValueIsNotObject() throws Exception {
        // exercise & verify
        assertThatThrownBy(() -> JsonValue.wrap(null).asObject())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This value is not an object");
    }
}