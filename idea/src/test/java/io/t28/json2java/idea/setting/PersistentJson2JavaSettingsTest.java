package io.t28.json2java.idea.setting;

import io.t28.json2java.core.Style;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static io.t28.json2java.idea.Assertions.assertThat;

public class PersistentJson2JavaSettingsTest {
    private PersistentJson2JavaSettings underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new PersistentJson2JavaSettings();
    }

    @Test
    public void getStateShouldReturnElement() throws Exception {
        // setup
        underTest.setStyle(Style.GSON)
                .setClassNamePrefix("Foo")
                .setClassNameSuffix("Bar");

        // exercise
        final Element actual = underTest.getState();

        // verify
        assertThat(actual)
                .isNotNull()
                .hasName("component")
                .hasAttribute("style", "GSON")
                .hasAttribute("classNamePrefix", "Foo")
                .hasAttribute("classNameSuffix", "Bar");
    }

    @Test
    public void loadStateShouldLoadFromElement() throws Exception {
        // setup
        final Element state = new Element("component");
        state.setAttribute("style", "JACKSON");
        state.setAttribute("classNamePrefix", "Foo");
        state.setAttribute("classNameSuffix", "Baz");

        // exercise
        underTest.loadState(state);

        // verify
        final Element actual = underTest.getState();
        assertThat(actual)
                .isNotNull()
                .hasName("component")
                .hasAttribute("style", "JACKSON")
                .hasAttribute("classNamePrefix", "Foo")
                .hasAttribute("classNameSuffix", "Baz");
    }

    @Test
    public void loadStateShouldUseDefaultValueWhenAttributeDoesNotExist() throws Exception {
        // setup
        final Element state = new Element("component");

        // exercise
        underTest.loadState(state);

        // verify
        final Element actual = underTest.getState();
        assertThat(actual)
                .isNotNull()
                .hasName("component")
                .hasAttribute("style", "NONE")
                .hasAttribute("classNamePrefix", "")
                .hasAttribute("classNameSuffix", "");
    }

    @Test
    public void noStateLoadedShouldUseDefaultValue() throws Exception {
        // exercise
        underTest.noStateLoaded();

        // verify
        final Element actual = underTest.getState();
        assertThat(actual)
                .isNotNull()
                .hasName("component")
                .hasAttribute("style", "NONE")
                .hasAttribute("classNamePrefix", "")
                .hasAttribute("classNameSuffix", "");
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