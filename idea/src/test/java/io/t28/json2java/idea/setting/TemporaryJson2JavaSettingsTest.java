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
                .setClassNameSuffix("Bar")
                .setGeneratedAnnotationEnabled(true)
                .setSuppressWarningsAnnotationEnabled(false);

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
                .setClassNameSuffix("Bar")
                .setGeneratedAnnotationEnabled(true)
                .setSuppressWarningsAnnotationEnabled(false);

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
                .setClassNameSuffix("Bar")
                .setGeneratedAnnotationEnabled(true)
                .setSuppressWarningsAnnotationEnabled(false);

        // exercise
        final String actual = underTest.getClassNameSuffix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected suffix to be <%s> but was <%s>", "Bar", actual)
                .isEqualTo("Bar");
    }

    @Test
    public void isGeneratedAnnotationEnabledShouldReturnFlagForGenerated() throws Exception {
        // setup
        underTest.setStyle(Style.GSON)
                .setClassNamePrefix("Foo")
                .setClassNameSuffix("Bar")
                .setGeneratedAnnotationEnabled(true)
                .setSuppressWarningsAnnotationEnabled(true);

        // exercise
        final boolean actual = underTest.isGeneratedAnnotationEnabled();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected setting to be true but was false")
                .isTrue();
    }

    @Test
    public void isSuppressWarningsAnnotationEnabledShouldReturnFlagForSuppressWarnings() throws Exception {
        // setup
        underTest.setStyle(Style.GSON)
                .setClassNamePrefix("Foo")
                .setClassNameSuffix("Bar")
                .setGeneratedAnnotationEnabled(true)
                .setSuppressWarningsAnnotationEnabled(true);

        // exercise
        final boolean actual = underTest.isSuppressWarningsAnnotationEnabled();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected setting to be true but was false")
                .isTrue();
    }
}