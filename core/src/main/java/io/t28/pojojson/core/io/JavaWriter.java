package io.t28.pojojson.core.io;

import com.squareup.javapoet.TypeSpec;
import io.t28.pojojson.core.io.exception.JavaWriteException;

import javax.annotation.Nonnull;

public interface JavaWriter {
    void write(@Nonnull String packageName, @Nonnull TypeSpec typeSpec) throws JavaWriteException;
}
