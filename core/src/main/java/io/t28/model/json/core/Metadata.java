package io.t28.model.json.core;

import io.t28.model.json.core.naming.CaseRule;
import io.t28.model.json.core.naming.NamingRule;
import org.immutables.value.Value;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Set;

@Value.Enclosing
@Value.Immutable
@SuppressWarnings("NullableProblems")
public interface Metadata {
    @Nonnull
    String getName();

    @Nonnull
    Set<Modifier> getModifiers();

    @Nonnull
    List<Property> getProperties();

    @Nonnull
    List<Metadata> getNestedMetadata();

    @Nonnull
    static ImmutableMetadata.Builder builder() {
        return ImmutableMetadata.builder();
    }

    @Nonnull
    @Value.Immutable
    interface Property {
        @Nonnull
        CaseRule getNameCase();

        @Nonnull
        String getName();

        @Nonnull
        NamingRule getClassNameRule();

        @Nonnull
        NamingRule getFieldNameRule();

        @Nonnull
        NamingRule getMethodNameRule();

        @Nonnull
        default String getClassName() {
            final CaseRule caseRule = getNameCase();
            final String name = getName();
            return getClassNameRule().format(caseRule, name);
        }

        @Nonnull
        default String getFieldName() {
            final CaseRule caseRule = getNameCase();
            final String name = getName();
            return getFieldNameRule().format(caseRule, name);
        }

        @Nonnull
        default String getMethodName() {
            final CaseRule caseRule = getNameCase();
            final String name = getName();
            return getMethodNameRule().format(caseRule, name);
        }

        @Nonnull
        static ImmutableMetadata.Property.Builder builder() {
            return ImmutableMetadata.Property.builder();

        }
    }
}
