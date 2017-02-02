package io.t28.pojojson.core;

import io.t28.pojojson.core.naming.NamingCase;
import io.t28.pojojson.core.naming.NamingStrategy;
import io.t28.pojojson.core.naming.defaults.ClassNameStrategy;
import io.t28.pojojson.core.naming.defaults.FieldNameStrategy;
import io.t28.pojojson.core.naming.defaults.MethodNameStrategy;
import io.t28.pojojson.core.naming.defaults.ParameterNameStrategy;
import org.immutables.value.Value;

import javax.annotation.Nonnull;
import java.io.File;

@Value.Immutable
@SuppressWarnings("NullableProblems")
public interface Context {
    @Nonnull
    @Value.Default
    default File sourceDirectory() {
        return new File("build/classes/main/generated");
    }

    @Nonnull
    @Value.Default
    default ClassStyle style() {
        return ClassStyle.MODEL;
    }

    @Nonnull
    @Value.Default
    default NamingCase nameCase() {
        return NamingCase.LOWER_SNAKE_CASE;
    }

    @Nonnull
    @Value.Default
    default NamingStrategy classNameStrategy() {
        return new ClassNameStrategy(nameCase());
    }

    @Nonnull
    @Value.Default
    default NamingStrategy fieldNameStrategy() {
        return new FieldNameStrategy(nameCase());
    }

    @Nonnull
    @Value.Default
    default NamingStrategy methodNameStrategy() {
        return new MethodNameStrategy(nameCase());
    }

    @Nonnull
    @Value.Default
    default NamingStrategy parameterNameStrategy() {
        return new ParameterNameStrategy(nameCase());
    }

    @Nonnull
    static ImmutableContext.Builder builder() {
        return ImmutableContext.builder();
    }

    @Nonnull
    static Context copyOf(@Nonnull Context context) {
        return ImmutableContext.copyOf(context);
    }
}
