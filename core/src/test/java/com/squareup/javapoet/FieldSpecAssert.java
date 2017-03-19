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
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

public class FieldSpecAssert extends AbstractAssert<FieldSpecAssert, FieldSpec> {
    public FieldSpecAssert(@Nullable FieldSpec actual) {
        super(actual, FieldSpecAssert.class);
    }

    @Nonnull
    public FieldSpecAssert hasType(@Nonnull TypeName expected) {
        isNotNull();

        final TypeName actual = this.actual.type;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected type name to be <%s>, but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public FieldSpecAssert hasName(@Nonnull String expected) {
        isNotNull();

        final String actual = this.actual.name;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected name to be <%s>, but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public FieldSpecAssert hasNoJavadoc() {
        isNotNull();

        final CodeBlock actual = this.actual.javadoc;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected javadoc to be empty, but was <%s>", actual)
                .isEqualTo(CodeBlock.builder().build());

        return this;
    }

    @Nonnull
    public FieldSpecAssert hasAnnotation(@Nonnull AnnotationSpec expected) {
        isNotNull();

        final List<AnnotationSpec> actual = this.actual.annotations;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expecting <%s> to contain but could not find <%s>", actual, expected)
                .contains(expected);

        return this;
    }

    @Nonnull
    public FieldSpecAssert hasNoAnnotation() {
        isNotNull();

        final List<AnnotationSpec> actual = this.actual.annotations;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected annotation to be empty, but was <%s>", actual)
                .isEmpty();

        return this;
    }

    @Nonnull
    public FieldSpecAssert isPrivate() {
        return hasModifier(Modifier.PRIVATE);
    }

    @Nonnull
    public FieldSpecAssert isFinal() {
        return hasModifier(Modifier.FINAL);
    }

    @Nonnull
    public FieldSpecAssert hasModifier(@Nonnull Modifier... expected) {
        isNotNull();

        final Set<Modifier> actual = this.actual.modifiers;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected modifier to be <%s>, but was <%s>", expected, actual)
                .contains(expected);

        return this;
    }

    @Nonnull
    public FieldSpecAssert hasNoInitializer() {
        isNotNull();

        final CodeBlock actual = this.actual.initializer;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected initializer to be empty, but was <%s>", actual)
                .isEqualTo(CodeBlock.builder().build());

        return this;
    }
}
