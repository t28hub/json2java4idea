package io.t28.jsoon.core.naming;

import org.immutables.value.Value;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public abstract class NamingRule {
    private static final String SNAKE_CASE_DELIMITER = "_";

    @Nonnull
    public abstract CaseRule rule();

    @Nonnull
    public abstract Optional<String> prefix();

    @Nonnull
    public abstract Optional<String> suffix();

    @Nonnull
    public static ImmutableNamingRule.Builder builder() {
        return ImmutableNamingRule.builder();
    }

    @Nonnull
    @CheckReturnValue
    public String format(@Nonnull CaseRule rule, @Nonnull String text) {
        // Convert to lower_snake_case temporary for appending prefix and suffix
        final StringBuilder builder = new StringBuilder(text.length());
        prefix().ifPresent(prefix -> builder.append(prefix).append(SNAKE_CASE_DELIMITER));
        builder.append(rule.to(CaseRule.LOWER_SNAKE_CASE, text));
        suffix().ifPresent(suffix -> builder.append(SNAKE_CASE_DELIMITER).append(suffix));
        return CaseRule.LOWER_CAMEL_CASE.to(rule(), builder.toString());
    }
}
