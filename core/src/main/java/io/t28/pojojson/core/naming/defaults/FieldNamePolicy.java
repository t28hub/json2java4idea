package io.t28.pojojson.core.naming.defaults;

import com.google.common.base.Strings;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.CasePolicy;
import io.t28.pojojson.core.naming.NamePolicy;
import io.t28.pojojson.core.utils.Keywords;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FieldNamePolicy implements NamePolicy {
    private static final String RESERVED_KEYWORD_PREFIX = "_";

    private final CasePolicy nameCase;
    private final String prefix;
    private final String suffix;

    public FieldNamePolicy(@Nonnull CasePolicy nameCase) {
        this(nameCase, null, null);
    }

    public FieldNamePolicy(@Nonnull CasePolicy nameCase, @Nullable String prefix, @Nullable String suffix) {
        this.nameCase = nameCase;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final StringBuilder builder = new StringBuilder(name.length());
        if (!Strings.isNullOrEmpty(prefix)) {
            builder.append(prefix).append(CasePolicy.KEBAB_CASE_DELIMITER);
        }

        builder.append(nameCase.convert(CasePolicy.UPPER_KEBAB_CASE, name));

        if (!Strings.isNullOrEmpty(suffix)) {
            builder.append(CasePolicy.KEBAB_CASE_DELIMITER).append(suffix);
        }

        final String fieldName = CasePolicy.UPPER_KEBAB_CASE.convert(CasePolicy.LOWER_CAMEL_CASE, builder.toString());
        if (Keywords.isReserved(fieldName)) {
            return RESERVED_KEYWORD_PREFIX + fieldName;
        }
        return fieldName;
    }
}
