package io.t28.model.json.core.naming.defaults;

import com.google.common.base.Strings;
import com.squareup.javapoet.TypeName;
import io.t28.model.json.core.naming.NamingCase;
import io.t28.model.json.core.naming.NamingStrategy;
import io.t28.model.json.core.utils.Keywords;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FieldNameStrategy implements NamingStrategy {
    private static final String RESERVED_KEYWORD_PREFIX = "_";

    private final NamingCase nameCase;
    private final String prefix;
    private final String suffix;

    public FieldNameStrategy(@Nonnull NamingCase nameCase) {
        this(nameCase, null, null);
    }

    public FieldNameStrategy(@Nonnull NamingCase nameCase, @Nullable String prefix, @Nullable String suffix) {
        this.nameCase = nameCase;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String transform(@Nonnull String name, @Nonnull TypeName type) {
        final StringBuilder builder = new StringBuilder(name.length());
        if (!Strings.isNullOrEmpty(prefix)) {
            builder.append(prefix).append(NamingCase.KEBAB_CASE_DELIMITER);
        }

        builder.append(nameCase.to(NamingCase.UPPER_KEBAB_CASE, name));

        if (!Strings.isNullOrEmpty(suffix)) {
            builder.append(NamingCase.KEBAB_CASE_DELIMITER).append(suffix);
        }

        final String fieldName = NamingCase.UPPER_KEBAB_CASE.to(NamingCase.LOWER_CAMEL_CASE, builder.toString());
        if (Keywords.isReserved(fieldName)) {
            return RESERVED_KEYWORD_PREFIX + fieldName;
        }
        return fieldName;
    }
}
