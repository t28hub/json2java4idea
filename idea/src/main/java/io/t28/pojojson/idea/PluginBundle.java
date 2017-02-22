package io.t28.pojojson.idea;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.PropertyKey;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class PluginBundle extends AbstractBundle {
    private static final String BUNDLE = "messages.PluginBundle";

    public PluginBundle() {
        super(BUNDLE);
    }

    @Nonnull
    @CheckReturnValue
    public String message(@Nonnull @PropertyKey(resourceBundle = BUNDLE) String key, @Nonnull Object... objects) {
        return getMessage(key, objects);
    }
}
