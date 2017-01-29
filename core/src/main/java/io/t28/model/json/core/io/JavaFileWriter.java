package io.t28.model.json.core.io;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.t28.model.json.core.io.exception.JavaWriteException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public class JavaFileWriter implements JavaWriter {
    private static final String INDENT = "    ";

    private final File directory;

    public JavaFileWriter(@Nonnull File directory) {
        this.directory = directory;
    }

    @Override
    public void write(@Nonnull String packageName, @Nonnull TypeSpec typeSpec) throws JavaWriteException {
        try {
            JavaFile.builder(packageName, typeSpec)
                    .indent(INDENT)
                    .skipJavaLangImports(true)
                    .build()
                    .writeTo(directory);
        } catch (IOException e) {
            throw new JavaWriteException("Failed to write Java to directory(" + directory + ")", e);
        }
    }
}
