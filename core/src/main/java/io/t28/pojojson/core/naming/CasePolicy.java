package io.t28.pojojson.core.naming;

import com.google.common.base.CaseFormat;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public enum CasePolicy {
    LOWER_CAMEL_CASE(CaseFormat.LOWER_CAMEL),
    UPPER_CAMEL_CASE(CaseFormat.UPPER_CAMEL),
    LOWER_SNAKE_CASE(CaseFormat.LOWER_UNDERSCORE),
    UPPER_SNAKE_CASE(CaseFormat.UPPER_UNDERSCORE),
    LOWER_KEBAB_CASE(CaseFormat.LOWER_HYPHEN),
    UPPER_KEBAB_CASE(CaseFormat.LOWER_HYPHEN) {
        @Nonnull
        @Override
        public String convert(@Nonnull CasePolicy sourcePolicy, @Nonnull String sourceText) {
            return super.convert(sourcePolicy, sourceText.toLowerCase());
        }
    };

    @SuppressWarnings("unused")
    public static final String SNAKE_CASE_DELIMITER = "_";

    @SuppressWarnings("unused")
    public static final String KEBAB_CASE_DELIMITER = "-";

    private final CaseFormat format;

    CasePolicy(@Nonnull CaseFormat format) {
        this.format = format;
    }

    @Nonnull
    @CheckReturnValue
    public String convert(@Nonnull CasePolicy sourcePolicy, @Nonnull String sourceText) {
        if (this == sourcePolicy) {
            return sourceText;
        }
        if (sourcePolicy == CasePolicy.UPPER_KEBAB_CASE) {
            return this.format.to(sourcePolicy.format, sourceText).toUpperCase();
        }
        return this.format.to(sourcePolicy.format, sourceText);
    }
}
