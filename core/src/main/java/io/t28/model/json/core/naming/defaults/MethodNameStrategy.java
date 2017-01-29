package io.t28.model.json.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;

import javax.annotation.Nonnull;

public class MethodNameStrategy implements NamingStrategy {
    private static final String BOOLEAN_PREFIX = "is";
    private static final String GENERAL_PREFIX = "get";

    private final NamingCase nameCase;

    public MethodNameStrategy(@Nonnull NamingCase nameCase) {
        this.nameCase = nameCase;
    }

    @Nonnull
    @Override
    public String transform(@Nonnull String name, @Nonnull TypeName type) {
        final StringBuilder builder = new StringBuilder();
        if (type.equals(TypeName.BOOLEAN)) {
            return builder.append(BOOLEAN_PREFIX)
                    .append(nameCase.to(NamingCase.UPPER_CAMEL_CASE, name))
                    .toString();
        }
        return builder.append(GENERAL_PREFIX)
                .append(nameCase.to(NamingCase.UPPER_CAMEL_CASE, name))
                .toString();
    }
}
