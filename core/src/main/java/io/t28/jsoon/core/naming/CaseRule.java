package io.t28.jsoon.core.naming;

import com.google.common.base.CaseFormat;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public enum CaseRule {
    LOWER_CAMEL_CASE(CaseFormat.LOWER_CAMEL),
    UPPER_CAMEL_CASE(CaseFormat.UPPER_CAMEL),
    LOWER_SNAKE_CASE(CaseFormat.LOWER_UNDERSCORE),
    UPPER_SNAKE_CASE(CaseFormat.UPPER_UNDERSCORE),
    LOWER_KEBAB_CASE(CaseFormat.LOWER_HYPHEN),
    UPPER_KEBAB_CASE(CaseFormat.LOWER_HYPHEN) {
        @Nonnull
        @Override
        public String to(@Nonnull CaseRule type, @Nonnull String text) {
            return super.to(type, text.toLowerCase());
        }
    };

    private final CaseFormat format;

    CaseRule(@Nonnull CaseFormat format) {
        this.format = format;
    }

    @Nonnull
    @CheckReturnValue
    public String to(@Nonnull CaseRule type, @Nonnull String text) {
        final String converted = this.format.to(type.format, text);
        if (type == UPPER_KEBAB_CASE) {
            return converted.toUpperCase();
        }
        return converted;
    }
}
