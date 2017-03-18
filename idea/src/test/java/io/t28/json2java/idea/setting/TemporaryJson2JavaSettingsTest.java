package io.t28.json2java.idea.setting;

import io.t28.json2java.core.Style;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TemporaryJson2JavaSettingsTest {
    private TemporaryJson2JavaSettings underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new TemporaryJson2JavaSettings();
    }

    @Test
    public void getStyleShouldReturnStyle() throws Exception {
        // setup
        underTest.setStyle(Style.GSON)
                .setClassNamePrefix("Foo")
                .setClassNameSuffix("Bar");

        // exercise
        final Style actual = underTest.getStyle();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected style to be <%s> but was <%s>", Style.GSON, actual)
                .isEqualTo(Style.GSON);
    }

    @Test
    public void getClassNamePrefixShouldReturnPrefix() throws Exception {
        // setup
        underTest.setStyle(Style.GSON)
                .setClassNamePrefix("Foo")
                .setClassNameSuffix("Bar");

        // exercise
        final String actual = underTest.getClassNamePrefix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected prefix to be <%s> but was <%s>", "Foo", actual)
                .isEqualTo("Foo");
    }

    @Test
    public void getClassNameSuffixShouldReturnSuffix() throws Exception {
        // setup
        underTest.setStyle(Style.GSON)
                .setClassNamePrefix("Foo")
                .setClassNameSuffix("Bar");

        // exercise
        final String actual = underTest.getClassNameSuffix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected suffix to be <%s> but was <%s>", "Bar", actual)
                .isEqualTo("Bar");
    }
}