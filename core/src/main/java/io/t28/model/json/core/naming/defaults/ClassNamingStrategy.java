package io.t28.model.json.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;

public class ClassNamingStrategy implements NamingStrategy {
    @Nonnull
    @Override
    public String transform(@Nonnull TypeName type, @Nonnull String name, @Nonnull NamingCase nameCase) {
        return nameCase.to(NamingCase.UPPER_CAMEL_CASE, name);
    }
}
