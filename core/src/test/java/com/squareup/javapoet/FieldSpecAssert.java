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
    public FieldSpecAssert hasNoAnnotation() {
        isNotNull();

        final List<AnnotationSpec> actual = this.actual.annotations;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected annotation to be empty, but was <%s>", actual)
                .isEmpty();

        return this;
    }

    @Nonnull
    public FieldSpecAssert hasModifier(@Nonnull Modifier... expected) {
        isNotNull();

        final Set<Modifier> actual = this.actual.modifiers;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected modifier to be <%s>, but was <%s>", expected, actual)
                .containsOnly(expected);

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
