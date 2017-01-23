package io.t28.jsoon.core.naming;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class DefaultNamingStrategy implements NamingStrategy {
    private static final String SNAKE_CASE_DELIMITER = "_";

    private final CaseRule nameRule;
    private final NamingRule classNamingRule;
    private final NamingRule fieldNamingRule;
    private final NamingRule methodNamingRule;

    public DefaultNamingStrategy(@Nonnull CaseRule nameRule,
                                 @Nonnull NamingRule classNamingRule,
                                 @Nonnull NamingRule fieldNamingRule,
                                 @Nonnull NamingRule methodNamingRule) {
        this.nameRule = nameRule;
        this.classNamingRule = classNamingRule;
        this.fieldNamingRule = fieldNamingRule;
        this.methodNamingRule = methodNamingRule;
    }

    @Nonnull
    @Override
    public String toClassName(@Nonnull String name) {
        return to(classNamingRule, name);
    }

    @Nonnull
    @Override
    public String toFieldName(@Nonnull String name) {
        return to(fieldNamingRule, name);
    }

    @Nonnull
    @Override
    public String toMethodName(@Nonnull String name) {
        return to(methodNamingRule, name);
    }

    @Nonnull
    @CheckReturnValue
    private String to(@Nonnull NamingRule namingRule, @Nonnull String text) {
        // Convert to lower_snake_case temporary for appending prefix and suffix
        final StringBuilder builder = new StringBuilder(text.length());
        namingRule.prefix().ifPresent(prefix -> builder.append(prefix).append(SNAKE_CASE_DELIMITER));
        builder.append(nameRule.to(CaseRule.LOWER_SNAKE_CASE, text));
        namingRule.suffix().ifPresent(suffix -> builder.append(SNAKE_CASE_DELIMITER).append(suffix));
        return CaseRule.LOWER_CAMEL_CASE.to(namingRule.rule(), builder.toString());
    }
}
