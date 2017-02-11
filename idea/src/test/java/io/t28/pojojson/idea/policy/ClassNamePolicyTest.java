package io.t28.pojojson.idea.policy;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassNamePolicyTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void parseLowerSnakeCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy("_");

        // exercise
        final List<String> parsed = underTest.parse("lower_snake_case_text");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("lower", "snake", "case", "text");
    }

    @Test
    public void parseUpperSnakeCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy("_");

        // exercise
        final List<String> parsed = underTest.parse("UPPER_SNAKE_CASE_TEXT");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("upper", "snake", "case", "text");
    }

    @Test
    public void parseLowerKebabCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy("-");

        // exercise
        final List<String> parsed = underTest.parse("lower-kebab-case-text");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("lower", "kebab", "case", "text");
    }

    @Test
    public void parseUpperKebabCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy("-");

        // exercise
        final List<String> parsed = underTest.parse("UPPER-KEBAB-CASE-TEXT");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("upper", "kebab", "case", "text");
    }

    @Test
    public void parseLowerSpaceCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy(" ");

        // exercise
        final List<String> parsed = underTest.parse("lower space case text");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("lower", "space", "case", "text");
    }

    @Test
    public void parseUpperSpaceCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy(" ");

        // exercise
        final List<String> parsed = underTest.parse("UPPER SPACE CASE TEXT");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("upper", "space", "case", "text");
    }

    @Test
    public void parseLowerCamelCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy();

        // exercise
        final List<String> parsed = underTest.parse("lowerCamelCaseText");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("lower", "camel", "case", "text");
    }

    @Test
    public void parseUpperCamelCaseText() throws Exception {
        // setup
        final ClassNamePolicy underTest = new ClassNamePolicy();

        // exercise
        final List<String> parsed = underTest.parse("UpperCamelCaseText");

        // verify
        assertThat(parsed)
                .hasSize(4)
                .containsOnlyOnce("upper", "camel", "case", "text");
    }
}