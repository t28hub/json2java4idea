package com.squareup.javapoet;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

public class MethodSpecAssert extends AbstractAssert<MethodSpecAssert, MethodSpec> {
    public MethodSpecAssert(@Nullable MethodSpec actual) {
        super(actual, MethodSpecAssert.class);
    }

    @Nonnull
    public MethodSpecAssert isConstructor() {
        isNotNull();

        final String actual = this.actual.name;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected to be constructor, but was method")
                .isEqualTo(MethodSpec.CONSTRUCTOR);

        return this;
    }

    @Nonnull
    public MethodSpecAssert isNotConstructor() {
        isNotNull();

        final String actual = this.actual.name;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected not to be constructor, but was constructor")
                .isNotEqualTo(MethodSpec.CONSTRUCTOR);

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasName(@Nonnull String expected) {
        isNotNull();

        final String actual = this.actual.name;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected name to be <%s>, but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasNoJavaDoc() {
        isNotNull();

        final CodeBlock actual = this.actual.javadoc;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected javadoc to be empty, but was <%s>", actual)
                .isEqualTo(CodeBlock.builder().build());

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasAnnotation(@Nonnull AnnotationSpec expected) {
        isNotNull();

        final List<AnnotationSpec> actual = this.actual.annotations;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expecting <%s> to contain but could not find <%s>", actual, expected)
                .contains(expected);

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasNoAnnotation() {
        isNotNull();

        final List<AnnotationSpec> actual = this.actual.annotations;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected annotation to be empty, but was <%s>", actual)
                .isEmpty();

        return this;
    }

    @Nonnull
    public MethodSpecAssert isPublic() {
        return hasModifier(Modifier.PUBLIC);
    }

    @Nonnull
    public MethodSpecAssert hasModifier(@Nonnull Modifier... expected) {
        isNotNull();

        final Set<Modifier> actual = this.actual.modifiers;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expecting <%s> to contain but could not find <%s>", actual, expected)
                .contains(expected);

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasReturnType(@Nonnull TypeName expected) {
        isNotNull();

        final TypeName actual = this.actual.returnType;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected return type to be <%s>, but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasParameter(@Nonnull TypeName expectedType, @Nonnull String expectedName) {
        return hasParameter(ParameterSpec.builder(expectedType, expectedName).build());
    }

    @Nonnull
    public MethodSpecAssert hasParameter(@Nonnull ParameterSpec expected) {
        isNotNull();

        final List<ParameterSpec> actual = this.actual.parameters;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expecting <%s> to contain but could not find <%s>", actual, expected)
                .contains(expected);

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasNoParameter() {
        isNotNull();

        final List<ParameterSpec> actual = this.actual.parameters;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected parameters to be empty, but was <%s>", actual)
                .isEmpty();

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasNoException() {
        isNotNull();

        final List<TypeName> actual = this.actual.exceptions;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected exception to be empty, but was <%s>", actual)
                .isEmpty();

        return this;
    }

    @Nonnull
    public MethodSpecAssert hasStatement(@Nonnull String expected) {
        return hasCode(CodeBlock.builder().addStatement(expected).build());
    }

    @Nonnull
    public MethodSpecAssert hasCode(@Nonnull CodeBlock expected) {
        isNotNull();

        final CodeBlock actual = this.actual.code;
        Assertions.assertThat(actual)
                .overridingErrorMessage("Expected code to be <%s>, but was <%s>", expected, actual)
                .isEqualTo(expected);

        return this;
    }
}
