package io.t28.pojojson.core.naming;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.squareup.javapoet.TypeName;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class DefaultNamePolicyTest {

    @RunWith(Theories.class)
    public static class CLASS {
        @DataPoints
        @SuppressWarnings({"unused", "unchecked"})
        public static Fixture<String, TypeName, String>[] FIXTURES = new Fixture[]{
                Fixture.builder()
                        .arg1("this is a lower space case")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsALowerSpaceCase")
                        .build(),
                Fixture.builder()
                        .arg1("THIS IS A UPPER SPACE CASE")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsAUpperSpaceCase")
                        .build(),
                Fixture.builder()
                        .arg1("this_is_a_lower_snake_case")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsALowerSnakeCase")
                        .build(),
                Fixture.builder()
                        .arg1("THIS_IS_A_UPPER_SNAKE_CASE")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsAUpperSnakeCase")
                        .build(),
                Fixture.builder()
                        .arg1("this-is-a-lower-kebab-case")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsALowerKebabCase")
                        .build(),
                Fixture.builder()
                        .arg1("THIS-IS-A-UPPER-KEBAB-CASE")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsAUpperKebabCase")
                        .build(),
                Fixture.builder()
                        .arg1("thisIsALowerCamelCase")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsALowerCamelCase")
                        .build(),
                Fixture.builder()
                        .arg1("ThisIsAUpperCamelCase")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsAUpperCamelCase")
                        .build(),
                Fixture.builder()
                        .arg1("THIS is-A mix-leTTer_CASe")
                        .arg2(TypeName.OBJECT)
                        .expected("ThisIsAMixLetterCase")
                        .build(),
                Fixture.builder()
                        .arg1("RaND0m Letter$ -case_!")
                        .arg2(TypeName.OBJECT)
                        .expected("Rand0mLetter$Case")
                        .build(),
                Fixture.builder()
                        .arg1("これはラテン文字ではありません。")
                        .arg2(TypeName.OBJECT)
                        .expected("")
                        .build()
    };


        @Theory
        public void convert(@Nonnull Fixture<String, TypeName, String> fixture) throws Exception {
            // exercise
            final String actual = DefaultNamePolicy.CLASS.convert(fixture.arg1, fixture.arg2);

            // verify
            assertThat(actual)
                    .isEqualTo(fixture.expected);
        }
    }

    @RunWith(Theories.class)
    public static class format {
        @DataPoints
        @SuppressWarnings({"unused", "unchecked"})
        public static Fixture<String, CaseFormat, String>[] FIXTURES = new Fixture[]{
                Fixture.of(
                        "this is a lower space case",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-lower-space-case"
                ),
                Fixture.of(
                        "THIS IS A UPPER SPACE CASE",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-upper-space-case"
                ),
                Fixture.of(
                        "this_is_a_lower_snake_case",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-lower-snake-case"
                ),
                Fixture.of(
                        "THIS_IS_A_UPPER_SNAKE_CASE",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-upper-snake-case"
                ),
                Fixture.of(
                        "this-is-a-lower-kebab-case",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-lower-kebab-case"
                ),
                Fixture.of(
                        "THIS-IS-A-UPPER-KEBAB-CASE",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-upper-kebab-case"
                ),
                Fixture.of(
                        "thisIsALowerCamelCase",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-lower-camel-case"
                ),
                Fixture.of(
                        "ThisIsAUpperCamelCase",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-upper-camel-case"
                ),
                Fixture.of(
                        "THIS is-A mix-leTTer_CASe",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-mix-letter-case"
                ),
                Fixture.of(
                        "THIS.iS_A RaND0m Letter$ -case_!",
                        CaseFormat.LOWER_HYPHEN,
                        "this-is-a-rand0m-letter$-case"
                ),
                Fixture.of(
                        "これはラテン文字ではありません。",
                        CaseFormat.LOWER_HYPHEN,
                        ""
                )
        };

        @Theory
        public void format(@Nonnull Fixture<String, CaseFormat, String> fixture) throws Exception {
            // exercise
            final String actual = DefaultNamePolicy.format(fixture.arg1, fixture.arg2);

            // verify
            assertThat(actual)
                    .isEqualTo(fixture.expected);
        }
    }

    static class Fixture<T1, T2, T3> {
        private final T1 arg1;
        private final T2 arg2;
        private final T3 expected;

        private Fixture(@Nonnull Builder<T1, T2, T3> builder) {
            this.arg1 = Preconditions.checkNotNull(builder.arg1);
            this.arg2 = Preconditions.checkNotNull(builder.arg2);
            this.expected = Preconditions.checkNotNull(builder.expected);
        }

        private Fixture(T1 arg1, T2 arg2, T3 expected) {
            this.arg1 = Preconditions.checkNotNull(arg1);
            this.arg2 = Preconditions.checkNotNull(arg2);
            this.expected = Preconditions.checkNotNull(expected);
        }

        @Nonnull
        static <T1, T2, T3> Fixture<T1, T2, T3> of(@Nonnull T1 arg1, @Nonnull T2 arg2, @Nonnull T3 expected) {
            return new Fixture<>(arg1, arg2, expected);
        }

        @Nonnull
        static <T1, T2, T3> Builder<T1, T2, T3> builder() {
            return new Builder<>();
        }

        static class Builder<T1, T2, T3> {
            private T1 arg1;
            private T2 arg2;
            private T3 expected;

            private Builder() {
            }

            @Nonnull
            Builder arg1(@Nonnull T1 arg1) {
                this.arg1 = arg1;
                return this;
            }

            @Nonnull
            Builder arg2(@Nonnull T2 arg2) {
                this.arg2 = arg2;
                return this;
            }

            @Nonnull
            Builder expected(@Nonnull T3 expected) {
                this.expected = expected;
                return this;
            }

            @Nonnull
            Fixture<T1, T2, T3> build() {
                return new Fixture<>(this);
            }
        }
    }
}