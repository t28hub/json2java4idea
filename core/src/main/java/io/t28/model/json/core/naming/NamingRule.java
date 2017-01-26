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
    @CheckReturnValue
    static ImmutableNamingRule.Builder builder() {
        return ImmutableNamingRule.builder();
    }
}
