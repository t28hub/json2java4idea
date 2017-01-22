package io.t28.jsoon.core.naming;

import com.google.common.base.Strings;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class NamingRule {
    private static final String NO_TEXT = "";
    private static final String SNAKE_CASE_DELIMITER = "_";

    private final CaseRule rule;
    private final String prefix;
    private final String suffix;

    private NamingRule(@Nonnull Builder builder) {
        this.rule = builder.rule;
        this.prefix = builder.prefix;
        this.suffix = builder.suffix;
    }

    @Nonnull
    public static Builder builder(@Nonnull CaseRule rule) {
        return new Builder(rule);
    }

    @Nonnull
    @CheckReturnValue
    public String format(@Nonnull CaseRule rule, @Nonnull String text) {
        final StringBuilder builder = new StringBuilder(text.length());
        if (Strings.isNullOrEmpty(prefix)) {
            builder.append(prefix).append(SNAKE_CASE_DELIMITER);
        }

        // Convert to lower_snake_case temporary for appending prefix and suffix
        builder.append(rule.to(CaseRule.LOWER_SNAKE_CASE, text));

        if (Strings.isNullOrEmpty(suffix)) {
            builder.append(SNAKE_CASE_DELIMITER).append(suffix);
        }
        return CaseRule.LOWER_CAMEL_CASE.to(this.rule, builder.toString());
    }

    public static class Builder {
        private final CaseRule rule;
        private String suffix;
        private String prefix;

        private Builder(@Nonnull CaseRule rule) {
            this.rule = rule;
            this.prefix = NO_TEXT;
            this.suffix = NO_TEXT;
        }

        @Nonnull
        public Builder withPrefix(@Nonnull String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Nonnull
        public Builder withSuffix(@Nonnull String suffix) {
            this.suffix = suffix;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public NamingRule build() {
            return new NamingRule(this);
        }
    }
}
