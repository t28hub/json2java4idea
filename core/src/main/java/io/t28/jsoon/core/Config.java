package io.t28.jsoon.core;

import io.t28.jsoon.core.naming.CaseRule;
import io.t28.jsoon.core.naming.NamingRule;
import org.immutables.value.Value;

import javax.annotation.Nonnull;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public interface Config {
    @Nonnull
    CaseRule nameRule();

    @Nonnull
    @Value.Default
    default NamingRule classRule() {
        return NamingRule.builder()
                .rule(CaseRule.UPPER_CAMEL_CASE)
                .build();
    }

    @Nonnull
    @Value.Default
    default NamingRule fieldRule() {
        return NamingRule.builder()
                .rule(CaseRule.LOWER_CAMEL_CASE)
                .build();
    }

    @Nonnull
    @Value.Default
    default NamingRule methodRule() {
        return NamingRule.builder()
                .rule(CaseRule.LOWER_CAMEL_CASE)
                .build();
    }

    @Nonnull
    static ImmutableConfig.Builder builder() {
        return ImmutableConfig.builder();
    }
}
