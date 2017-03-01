package io.t28.json2java.idea.utils;

import com.google.inject.Inject;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

public class Formatter {
    private static final String NAME = "temporary";

    private final PsiFileFactory fileFactory;

    @Inject
    public Formatter(@Nonnull PsiFileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    @NotNull
    @CheckReturnValue
    public String format(@Nonnull String text, @Nonnull FileType type) {
        final String name = Extensions.append(NAME, type);
        final PsiFile file = fileFactory.createFileFromText(name, type, text);
        final CodeStyleManager styleManager = CodeStyleManager.getInstance(file.getProject());
        styleManager.reformat(file);
        return file.getText();
    }
}
