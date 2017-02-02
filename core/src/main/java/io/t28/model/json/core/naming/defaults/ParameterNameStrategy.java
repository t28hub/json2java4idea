package io.t28.model.json.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;
import io.t28.model.json.core.utils.Keywords;

import javax.annotation.Nonnull;

public class ParameterNameStrategy implements NamingStrategy {
    private static final String RESERVED_KEYWORD_PREFIX = "_";

    private final NamingCase nameCase;

    public ParameterNameStrategy(@Nonnull NamingCase nameCase) {
        this.nameCase = nameCase;
    }

    @Nonnull
    @Override
    public String transform(@Nonnull String name, @Nonnull TypeName type) {
        final String propertyName = nameCase.to(NamingCase.LOWER_CAMEL_CASE, name);
        if (Keywords.isReserved(propertyName)) {
            return RESERVED_KEYWORD_PREFIX + propertyName;
        }
        return propertyName;
    }
}
