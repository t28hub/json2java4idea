package io.t28.jsoon.core.naming;

import org.immutables.value.Value;

import javax.annotation.Nonnull;
import java.util.Optional;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public abstract class NamingRule {
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
}
