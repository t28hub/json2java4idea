package io.t28.model.json.core.naming;

import com.google.common.base.CaseFormat;

import javax.annotation.Nonnull;

public enum NamingCase {
    LOWER_CAMEL_CASE(CaseFormat.LOWER_CAMEL),
    UPPER_CAMEL_CASE(CaseFormat.UPPER_CAMEL),
    LOWER_SNAKE_CASE(CaseFormat.LOWER_UNDERSCORE),
    UPPER_SNAKE_CASE(CaseFormat.UPPER_UNDERSCORE),
    LOWER_KEBAB_CASE(CaseFormat.LOWER_HYPHEN),
    UPPER_KEBAB_CASE(CaseFormat.LOWER_HYPHEN) {
        @Nonnull
        @Override
        public String to(@Nonnull NamingCase rule, @Nonnull String text) {
            return super.to(rule, text.toLowerCase());
        }
    };

    private final CaseFormat format;

    NamingCase(@Nonnull CaseFormat format) {
        this.format = format;
    }

    @Nonnull
    public String to(@Nonnull NamingCase rule, @Nonnull String text) {
        if (this == rule) {
            return text;
        }
        if (rule == NamingCase.UPPER_KEBAB_CASE) {
            return this.format.to(rule.format, text).toUpperCase();
        }
        return this.format.to(rule.format, text);
    }
}
