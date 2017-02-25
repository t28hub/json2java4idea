package io.t28.json2java.core.io;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.t28.json2java.core.io.JavaBuilder;
import io.t28.json2java.core.io.exception.JavaBuildException;

import javax.annotation.Nonnull;

public class JavaBuilderImpl implements JavaBuilder {
    private static final String INDENT = "    ";

    @Nonnull
    @Override
    public String build(@Nonnull String packageName, @Nonnull TypeSpec typeSpec) throws JavaBuildException {
        return JavaFile.builder(packageName, typeSpec)
                .indent(INDENT)
                .skipJavaLangImports(true)
                .build()
                .toString();
    }
}
