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

package io.t28.json2java.idea.util;

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