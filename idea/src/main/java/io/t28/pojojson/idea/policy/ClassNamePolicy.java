package io.t28.pojojson.idea.policy;

import com.google.common.annotations.VisibleForTesting;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.naming.CasePolicy;
import io.t28.pojojson.core.naming.NamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassNamePolicy implements NamePolicy {
    private static final String CAMEL_CASE_DELIMITER = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";
    private static final String SNAKE_CASE_DELIMITER = "_";

    private final String delimiter;

    public ClassNamePolicy() {
        this(CAMEL_CASE_DELIMITER);
    }

    @VisibleForTesting
    ClassNamePolicy(@Nonnull String delimiter) {
        this.delimiter = delimiter;
    }

    @Nonnull
    @Override
    public String convert(@Nonnull String name, @Nonnull TypeName type) {
        final String joined = parse(name).stream().collect(Collectors.joining(SNAKE_CASE_DELIMITER));
        return CasePolicy.UPPER_CAMEL_CASE.convert(CasePolicy.LOWER_SNAKE_CASE, joined);
    }

    @Nonnull
    @CheckReturnValue
    protected List<String> parse(@Nonnull String name) {
        return Stream.of(name.split(delimiter))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
