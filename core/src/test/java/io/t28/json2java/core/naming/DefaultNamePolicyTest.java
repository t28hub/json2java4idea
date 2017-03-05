package io.t28.json2java.core.naming;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.jooq.lambda.tuple.Tuple4;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class DefaultNamePolicyTest {
    @RunWith(Parameterized.class)
    public static class EnumTest {
        @Parameters(name = "{0}")
        public static Collection<Tuple4<DefaultNamePolicy, String, TypeName, String>> fixtures() {
            return Arrays.asList(
                    Tuple.tuple(
                            DefaultNamePolicy.CLASS,
                            "class_name",
                            TypeName.OBJECT,
                            "ClassName"
                    ),
                    Tuple.tuple(
                            DefaultNamePolicy.METHOD,
                            "method_name",
                            TypeName.OBJECT,
                            "getMethodName"
                    ),
                    Tuple.tuple(
                            DefaultNamePolicy.METHOD,
                            "method_name",
                            TypeName.BOOLEAN,
                            "isMethodName"
                    ),
                    Tuple.tuple(
                            DefaultNamePolicy.METHOD,
                            "method_name",
                            ClassName.get(Boolean.class),
                            "isMethodName"
                    ),
                    Tuple.tuple(
                            DefaultNamePolicy.FIELD,
                            "field_name",
                            TypeName.OBJECT,
                            "fieldName"
                    ),
                    Tuple.tuple(
                            DefaultNamePolicy.PARAMETER,
                            "parameter_name",
                            TypeName.OBJECT,
                            "parameterName"
                    )
            );
        }

        private final DefaultNamePolicy underTest;
        private final String name;
        private final TypeName type;
        private final String expected;

        public EnumTest(@Nonnull Tuple4<DefaultNamePolicy, String, TypeName, String> fixture) {
            underTest = fixture.v1();
            name = fixture.v2();
            type = fixture.v3();
            expected = fixture.v4();
        }

        @Test
        public void convert() throws Exception {
            // exercise
            final String actual = underTest.convert(name, type);

            // verify
            assertThat(actual).isEqualTo(expected);
        }
    }

    @RunWith(Parameterized.class)
    public static class FormatTest {
        @Parameters(name = "{0}")
        public static Collection<Tuple3<String, CaseFormat, String>> fixtures() {
            return Arrays.asList(
                    Tuple.tuple(
                            "this is a lower space case",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-lower-space-case"
                    ),
                    Tuple.tuple(
                            "THIS IS A UPPER SPACE CASE",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-upper-space-case"
                    ),
                    Tuple.tuple(
                            "this_is_a_lower_snake_case",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-lower-snake-case"
                    ),
                    Tuple.tuple(
                            "THIS_IS_A_UPPER_SNAKE_CASE",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-upper-snake-case"
                    ),
                    Tuple.tuple(
                            "this-is-a-lower-kebab-case",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-lower-kebab-case"
                    ),
                    Tuple.tuple(
                            "THIS-IS-A-UPPER-KEBAB-CASE",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-upper-kebab-case"
                    ),
                    Tuple.tuple(
                            "thisIsALowerCamelCase",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-lower-camel-case"
                    ),
                    Tuple.tuple(
                            "ThisIsAUpperCamelCase",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-upper-camel-case"
                    ),
                    Tuple.tuple(
                            "THIS is-A mix-leTTer_CASe",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-mix-letter-case"
                    ),
                    Tuple.tuple(
                            "THIS.iS_A RaND0m Letter$ -case_!",
                            CaseFormat.LOWER_HYPHEN,
                            "this-is-a-rand0m-letter$-case"
                    ),
                    Tuple.tuple(
                            "これはラテン文字ではありません。",
                            CaseFormat.LOWER_HYPHEN,
                            ""
                    )
            );
        }

        private final String name;
        private final CaseFormat format;
        private final String expected;

        public FormatTest(@Nonnull Tuple3<String, CaseFormat, String> fixture) {
            name = fixture.v1();
            format = fixture.v2();
            expected = fixture.v3();
        }

        @Test
        public void format() throws Exception {
            // exercise
            final String actual = DefaultNamePolicy.format(name, format);

            // verify
            assertThat(actual).isEqualTo(expected);
        }
    }

    @RunWith(JUnit4.class)
    public static class MethodTest {
        @Test
        public void valueOf() throws Exception {
            // exercise
            final DefaultNamePolicy actual = DefaultNamePolicy.valueOf("CLASS");

            // verify
            assertThat(actual).isEqualTo(DefaultNamePolicy.CLASS);
        }

        @Test
        public void values() throws Exception {
            // exercise
            final DefaultNamePolicy[] actual = DefaultNamePolicy.values();

            // verify
            assertThat(actual)
                    .hasSize(4)
                    .containsOnlyOnce(
                            DefaultNamePolicy.CLASS,
                            DefaultNamePolicy.FIELD,
                            DefaultNamePolicy.METHOD,
                            DefaultNamePolicy.PARAMETER
                    );
        }
    }
}