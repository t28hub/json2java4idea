package io.t28.json2java.idea;

import org.jdom.Element;
import org.jdom.ElementAssert;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Assertions {
    private Assertions() {
    }

    @Nonnull
    @CheckReturnValue
    public static ElementAssert assertThat(@Nullable Element actual) {
        return new ElementAssert(actual);
    }
}
