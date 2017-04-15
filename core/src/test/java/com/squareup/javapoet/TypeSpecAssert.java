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

package com.squareup.javapoet;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TypeSpecAssert extends AbstractAssert<TypeSpecAssert, TypeSpec> {
    public TypeSpecAssert(@Nullable TypeSpec actual) {
        super(actual, TypeSpecAssert.class);
    }

    @Nonnull
    public TypeSpecAssert hasName(@Nonnull String expected) {
        isNotNull();

        final String actual = this.actual.name;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected name to be <%s>, but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public TypeSpecAssert hasAnnotation(@Nonnull AnnotationSpec expected) {
        isNotNull();

        final List<AnnotationSpec> actual = this.actual.annotations;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expecting <%s> to contain but could not find <%s>", actual, expected)
                .contains(expected);

        return this;
    }
}
