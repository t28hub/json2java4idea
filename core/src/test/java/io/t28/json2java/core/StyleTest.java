package io.t28.json2java.core;

import io.t28.json2java.core.builder.ClassBuilder;
import io.t28.json2java.core.builder.GsonClassBuilder;
import io.t28.json2java.core.builder.JacksonClassBuilder;
import io.t28.json2java.core.builder.ModelClassBuilder;
import io.t28.json2java.core.builder.MoshiClassBuilder;
import io.t28.json2java.core.naming.DefaultNamePolicy;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static io.t28.json2java.core.Assertions.assertThat;

@RunWith(Enclosed.class)
public class StyleTest {
    private StyleTest() {
    }

    @RunWith(Parameterized.class)
    public static class ToBuilderTest {
        @Parameters(name = "should return \"{1}\" when \"{0}\"")
        public static Collection<Object[]> fixtures() {
            return Arrays.asList(new Object[][]{
                    {Style.NONE, ModelClassBuilder.class},
                    {Style.GSON, GsonClassBuilder.class},
                    {Style.JACKSON, JacksonClassBuilder.class},
                    {Style.MOSHI, MoshiClassBuilder.class},
            });
        }

        private final Style underTest;
        private final Class expected;

        public ToBuilderTest(Style underTest, Class expected) {
            this.underTest = underTest;
            this.expected = expected;
        }

        @Test
        public void toBuilder() throws Exception {
            // exercise
            final ClassBuilder actual = underTest.toBuilder(
                    DefaultNamePolicy.FIELD,
                    DefaultNamePolicy.METHOD,
                    DefaultNamePolicy.PARAMETER
            );

            // verify
            assertThat(actual)
                    .isInstanceOf(expected);
        }
    }

    @RunWith(JUnit4.class)
    public static class FromNameTest {
        @Test
        public void fromNameShouldReturnOptionalStyle() throws Exception {
            // exercise
            final Optional<Style> actual = Style.fromName("gson");

            // verify
            assertThat(actual)
                    .isNotEmpty()
                    .hasValue(Style.GSON);
        }

        @Test
        public void fromNameShouldReturnStyleIgnoringCase() throws Exception {
            // exercise
            final Optional<Style> actual = Style.fromName("JackSon");

            // verify
            assertThat(actual)
                    .isNotEmpty()
                    .hasValue(Style.JACKSON);
        }

        @Test
        public void fromNameShouldReturnEmptyWhenNoMatchFound() throws Exception {
            // exercise
            final Optional<Style> actual = Style.fromName("unknown");

            // verify
            assertThat(actual)
                    .isEmpty();
        }

        @Test
        public void fromNameShouldReturnStyle() throws Exception {
            // exercise
            final Style actual = Style.fromName("gson", Style.NONE);

            // verify
            assertThat(actual)
                    .isEqualTo(Style.GSON);
        }

        @Test
        public void fromNameShouldReturnDefault() throws Exception {
            // exercise
            final Style actual = Style.fromName("unknown", Style.NONE);

            // verify
            assertThat(actual)
                    .isEqualTo(Style.NONE);
        }
    }
}