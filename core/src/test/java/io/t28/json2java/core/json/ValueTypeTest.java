package io.t28.json2java.core.json;

import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static io.t28.json2java.core.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Enclosed.class)
public class ValueTypeTest {
    @RunWith(Parameterized.class)
    public static class IsAcceptableTest {
        @Parameters(name = "{0}")
        public static Collection<Tuple3<ValueType, Object, Boolean>> fixtures() {
            return Arrays.asList(
                    Tuple.tuple(ValueType.NULL, null, true),
                    Tuple.tuple(ValueType.NULL, "null", false),
                    Tuple.tuple(ValueType.BOOLEAN, true, true),
                    Tuple.tuple(ValueType.BOOLEAN, "true", false),
                    Tuple.tuple(ValueType.INT, 1024, true),
                    Tuple.tuple(ValueType.INT, 1024L, false),
                    Tuple.tuple(ValueType.LONG, 1024L, true),
                    Tuple.tuple(ValueType.LONG, 1024, false),
                    Tuple.tuple(ValueType.FLOAT, 1.0f, true),
                    Tuple.tuple(ValueType.FLOAT, 1.0d, false),
                    Tuple.tuple(ValueType.DOUBLE, 1.0d, true),
                    Tuple.tuple(ValueType.DOUBLE, 1.0f, false),
                    Tuple.tuple(ValueType.BIG_INTEGER, BigInteger.ONE, true),
                    Tuple.tuple(ValueType.BIG_INTEGER, 1, false),
                    Tuple.tuple(ValueType.BIG_DECIMAL, BigDecimal.ONE, true),
                    Tuple.tuple(ValueType.BIG_DECIMAL, 1, false),
                    Tuple.tuple(ValueType.STRING, "text", true),
                    Tuple.tuple(ValueType.STRING, null, false),
                    Tuple.tuple(ValueType.ARRAY, Collections.singletonList("test"), true),
                    Tuple.tuple(ValueType.ARRAY, null, false),
                    Tuple.tuple(ValueType.OBJECT, Collections.singletonMap("key", "test"), true),
                    Tuple.tuple(ValueType.OBJECT, null, false)
            );
        }

        private final ValueType underTest;
        private final Object value;
        private final boolean expected;

        public IsAcceptableTest(@Nonnull Tuple3<ValueType, Object, Boolean> fixture) {
            underTest = fixture.v1();
            value = fixture.v2();
            expected = fixture.v3();
        }

        @Test
        public void isAcceptable() throws Exception {
            // exercise
            final boolean actual = underTest.isAcceptable(value);

            // verify
            assertThat(actual).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class NormalWrapTest {
        @Parameters(name = "{0}")
        public static Collection<Tuple3<ValueType, Object, Class>> fixtures() {
            return Arrays.asList(
                    Tuple.tuple(ValueType.NULL, null, JsonNull.class),
                    Tuple.tuple(ValueType.BOOLEAN, true, JsonBoolean.class),
                    Tuple.tuple(ValueType.INT, 42, JsonNumber.class),
                    Tuple.tuple(ValueType.LONG, 42L, JsonNumber.class),
                    Tuple.tuple(ValueType.FLOAT, 4.2f, JsonNumber.class),
                    Tuple.tuple(ValueType.DOUBLE, 4.2d, JsonNumber.class),
                    Tuple.tuple(ValueType.BIG_INTEGER, BigInteger.ONE, JsonNumber.class),
                    Tuple.tuple(ValueType.BIG_DECIMAL, BigDecimal.ONE, JsonNumber.class),
                    Tuple.tuple(ValueType.STRING, "foo", JsonString.class),
                    Tuple.tuple(ValueType.ARRAY, Collections.emptyList(), JsonArray.class),
                    Tuple.tuple(ValueType.OBJECT, Collections.emptyMap(), JsonObject.class)
            );
        }

        private final ValueType underTest;
        private final Object value;
        private final Class expected;

        public NormalWrapTest(@Nonnull Tuple3<ValueType, Object, Class> fixture) {
            this.underTest = fixture.v1();
            this.value = fixture.v2();
            this.expected = fixture.v3();
        }

        @Test
        public void wrap() throws Exception {
            // exercise
            final JsonValue actual = underTest.wrap(value);

            // verify
            assertThat(actual)
                    .isNotNull()
                    .isInstanceOf(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class NonNormalWrapTest {
        @Parameters(name = "{0}")
        public static Collection<Tuple3<ValueType, Object, String>> fixtures() {
            return Arrays.asList(
                    Tuple.tuple(ValueType.NULL, "null", "Value 'null' is not a null"),
                    Tuple.tuple(ValueType.BOOLEAN, null, "Value 'null' is not a boolean"),
                    Tuple.tuple(ValueType.INT, null, "Value 'null' is not an int"),
                    Tuple.tuple(ValueType.LONG, null, "Value 'null' is not a long"),
                    Tuple.tuple(ValueType.FLOAT, null, "Value 'null' is not a float"),
                    Tuple.tuple(ValueType.DOUBLE, null, "Value 'null' is not a double"),
                    Tuple.tuple(ValueType.BIG_INTEGER, null, "Value 'null' is not a big integer"),
                    Tuple.tuple(ValueType.BIG_DECIMAL, null, "Value 'null' is not a big decimal"),
                    Tuple.tuple(ValueType.STRING, null, "Value 'null' is not a string"),
                    Tuple.tuple(ValueType.ARRAY, null, "Value 'null' is not an array"),
                    Tuple.tuple(ValueType.OBJECT, null, "Value 'null' is not an object")
            );
        }

        private final ValueType underTest;
        private final Object value;
        private final String expected;

        public NonNormalWrapTest(@Nonnull Tuple3<ValueType, Object, String> fixture) {
            underTest = fixture.v1();
            value = fixture.v2();
            expected = fixture.v3();
        }

        @Test
        public void wrap() throws Exception {
            // exercise & verify
            assertThatThrownBy(() -> underTest.wrap(value))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(expected);
        }
    }
}