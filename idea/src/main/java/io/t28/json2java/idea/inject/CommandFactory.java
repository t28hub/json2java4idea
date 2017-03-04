package io.t28.json2java.idea.inject;

import com.google.inject.assistedinject.Assisted;
import com.intellij.psi.PsiDirectory;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.idea.commands.NewClassCommand;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public interface CommandFactory {
    @Nonnull
    @CheckReturnValue
    NewClassCommand create(
            @Nonnull @Assisted("Name") String name,
            @Nonnull @Assisted("Json") String json,
            @Nonnull PsiDirectory directory,
            @Nonnull JavaConverter converter
    );
}
