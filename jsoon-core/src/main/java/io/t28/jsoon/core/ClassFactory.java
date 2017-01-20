package io.t28.jsoon.core;

import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;

public interface ClassFactory {
    @Nonnull
    TypeSpec create();
}
