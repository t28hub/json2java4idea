package io.t28.pojojson.core.json;

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

import static org.assertj.core.api.Assertions.assertThat;

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
}