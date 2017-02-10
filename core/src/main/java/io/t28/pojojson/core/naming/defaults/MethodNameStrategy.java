package io.t28.pojojson.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.CasePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.Nonnull;

public class MethodNameStrategy implements NamePolicy {
    private static final String BOOLEAN_PREFIX = "is";
    private static final String GENERAL_PREFIX = "get";

    private final CasePolicy nameCase;

    public MethodNameStrategy(@Nonnull CasePolicy nameCase) {
        this.nameCase = nameCase;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final StringBuilder builder = new StringBuilder();
        if (type.equals(TypeName.BOOLEAN)) {
            return builder.append(BOOLEAN_PREFIX)
                    .append(nameCase.convert(CasePolicy.UPPER_CAMEL_CASE, name))
                    .toString();
        }
        return builder.append(GENERAL_PREFIX)
                .append(nameCase.convert(CasePolicy.UPPER_CAMEL_CASE, name))
                .toString();
    }
}
