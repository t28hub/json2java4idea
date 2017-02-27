package io.t28.json2java.idea;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.PropertyKey;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class Json2JavaBundle extends AbstractBundle {
    private static final String BUNDLE = "messages.Json2JavaBundle";

    public Json2JavaBundle() {
        super(BUNDLE);
    }

    @Nonnull
    public static Json2JavaBundle getInstance() {
        return new Json2JavaBundle();
    }

    @Nonnull
    @CheckReturnValue
    public String message(@Nonnull @PropertyKey(resourceBundle = BUNDLE) String key, @Nonnull Object... objects) {
        return getMessage(key, objects);
    }
}
