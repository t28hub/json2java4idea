package io.t28.pojojson.core.naming.defaults;

import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.CasePolicy;
import io.t28.pojojson.core.naming.NamePolicy;
import io.t28.pojojson.core.utils.Keywords;

import javax.annotation.Nonnull;

public class ParameterNamePolicy implements NamePolicy {
    private static final String RESERVED_KEYWORD_PREFIX = "_";

    private final CasePolicy nameCase;

    public ParameterNamePolicy(@Nonnull CasePolicy nameCase) {
        this.nameCase = nameCase;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String propertyName = nameCase.convert(CasePolicy.LOWER_CAMEL_CASE, name);
        if (Keywords.isReserved(propertyName)) {
            return RESERVED_KEYWORD_PREFIX + propertyName;
        }
        return propertyName;
    }
}
