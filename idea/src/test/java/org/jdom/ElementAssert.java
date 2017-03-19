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

package org.jdom;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementAssert extends AbstractAssert<ElementAssert, Element> {
    public ElementAssert(@Nullable Element actual) {
        super(actual, ElementAssert.class);
    }

    @Nonnull
    public ElementAssert hasName(@Nonnull String expected) {
        isNotNull();

        final String actual = this.actual.getName();
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected root element to be named <%s> but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public ElementAssert hasAttribute(@Nonnull String expectedName, @Nonnull String expectedValue) {
        isNotNull();

        final Attribute actual = this.actual.getAttribute(expectedName);
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected attribute named <%s> does not exist", expectedName)
                .isNotNull();

        final String actualName = actual.getName();
        Assertions.assertThat(actualName)
                .overridingErrorMessage("Expected attribute name to be <%s> but was <%s>", expectedName, actualName)
                .isEqualTo(expectedName);

        final String actualValue = actual.getValue();
        Assertions.assertThat(actualValue)
                .overridingErrorMessage("Expected attribute value to be <%s> but was <%s>", expectedValue, actualValue)
                .isEqualTo(expectedValue);

        return this;
    }
}
