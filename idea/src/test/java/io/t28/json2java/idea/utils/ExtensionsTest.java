package io.t28.json2java.idea.utils;

import com.intellij.ide.highlighter.JavaFileType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtensionsTest {
    @Test
    public void removeShouldRemoveExtension() throws Exception {
        // exercise
        final String actual = Extensions.remove("foo.java", JavaFileType.INSTANCE);

        // verify
        assertThat(actual)
                .isEqualTo("foo");
    }

    @Test
    public void removeShouldNotRemoveExtensionWhenNotMatch() throws Exception {
        // exercise
        final String actual = Extensions.remove("foo.java.tmp", JavaFileType.INSTANCE);

        // verify
        assertThat(actual)
                .isEqualTo("foo.java.tmp");
    }

    @Test
    public void removeShouldReturnFileNameWhenExtensionDoesNotExist() throws Exception {
        // exercise
        final String actual = Extensions.remove("foo", JavaFileType.INSTANCE);

        // verify
        assertThat(actual)
                .isEqualTo("foo");
    }

    @Test
    public void appendShouldAppendExtension() throws Exception {
        // exercise
        final String actual = Extensions.append("foo", JavaFileType.INSTANCE);

        // verify
        assertThat(actual)
                .isEqualTo("foo.java");
    }

    @Test
    public void appendShouldNotAppendExtensionWhenExtensionExists() throws Exception {
        // exercise
        final String actual = Extensions.append("foo.java", JavaFileType.INSTANCE);

        // verify
        assertThat(actual)
                .isEqualTo("foo.java");
    }

    @Test
    public void appendShouldAppendExtensionWhenAnotherExtensionExists() throws Exception {
        // exercise
        final String actual = Extensions.append("foo.tmp", JavaFileType.INSTANCE);

        // verify
        assertThat(actual)
                .isEqualTo("foo.tmp.java");
    }
}