package io.t28.pojojson.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.NamingCase;
import io.t28.pojojson.core.naming.NamingStrategy;

import javax.annotation.Nonnull;

public class ClassNameStrategy implements NamingStrategy {
    private final NamingCase nameCase;

    public ClassNameStrategy(@Nonnull NamingCase nameCase) {
        this.nameCase = nameCase;
    }

    @Nonnull
    @Override
    public String transform(@Nonnull String name, @Nonnull TypeName type) {
        return nameCase.to(NamingCase.UPPER_CAMEL_CASE, name);
    }
}
