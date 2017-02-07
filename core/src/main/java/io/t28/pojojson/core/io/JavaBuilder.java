package io.t28.pojojson.core.io;

import com.squareup.javapoet.TypeSpec;
import io.t28.pojojson.core.io.exception.JavaBuildException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface JavaBuilder {
    @Nonnull
    @CheckReturnValue
    String build(@Nonnull String packageName, @Nonnull TypeSpec typeSpec) throws JavaBuildException;
}
