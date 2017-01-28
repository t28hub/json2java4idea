package io.t28.model.json.core.naming;

import org.immutables.value.Value;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.Optional;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public interface NamingRule {
    @Nonnull
    @CheckReturnValue
    CaseRule rule();

    @Nonnull
    @CheckReturnValue
    Optional<String> prefix();

    @Nonnull
    @CheckReturnValue
    Optional<String> suffix();

    @Nonnull
    default String format(@Nonnull CaseRule rule, @Nonnull String name) {
        final StringBuilder builder = new StringBuilder(name.length());
        builder.append(rule.to(CaseRule.UPPER_KEBAB_CASE, name));
        prefix().ifPresent(prefix -> builder.append(prefix).append("-"));
        suffix().ifPresent(suffix -> builder.append("-").append(suffix));
        return CaseRule.UPPER_KEBAB_CASE.to(rule(), builder.toString());
    }

    @Nonnull
    @CheckReturnValue
    static ImmutableNamingRule.Builder builder() {
        return ImmutableNamingRule.builder();
    }
}
