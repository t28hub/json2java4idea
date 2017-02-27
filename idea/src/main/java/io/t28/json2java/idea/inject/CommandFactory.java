package io.t28.json2java.idea.inject;

import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDirectory;
import io.t28.json2java.idea.commands.NewClassCommand;
import io.t28.json2java.idea.commands.FormatJsonCommand;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CommandFactory {
    @Nonnull
    @CheckReturnValue
    FormatJsonCommand create(@Nonnull String json,
                             @Nonnull Editor editor,
                             @Nonnull Document document
    );

    @Nonnull
    @CheckReturnValue
    NewClassCommand create(
            @Nonnull @Assisted("Name") String name,
            @Nonnull @Assisted("Json") String json,
            @Nullable PsiDirectory directory
    );
}
