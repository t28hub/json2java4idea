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
    NamingCase namingCase();

    @Nonnull
    @CheckReturnValue
    Optional<String> prefix();

    @Nonnull
    @CheckReturnValue
    Optional<String> suffix();

    @Nonnull
    default String format(@Nonnull NamingCase fromCase, @Nonnull String name) {
        final StringBuilder builder = new StringBuilder(name.length());
        prefix().ifPresent(prefix -> builder.append(prefix).append("-"));
        builder.append(fromCase.to(NamingCase.UPPER_KEBAB_CASE, name));
        suffix().ifPresent(suffix -> builder.append("-").append(suffix));
        return NamingCase.UPPER_KEBAB_CASE.to(namingCase(), builder.toString());
    }

    @Nonnull
    @CheckReturnValue
    static ImmutableNamingRule.Builder builder() {
        return ImmutableNamingRule.builder();
    }
}
