package io.t28.json2java.idea.util;

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
    private final FileType fileType;

    public Formatter(@Nonnull PsiFileFactory fileFactory, @Nonnull FileType fileType) {
        this.fileFactory = fileFactory;
        this.fileType = fileType;
    }

    @NotNull
    @CheckReturnValue
    public String format(@Nonnull String text) {
        final String name = Extensions.append(NAME, fileType);
        final PsiFile file = fileFactory.createFileFromText(name, fileType, text);
        final CodeStyleManager styleManager = CodeStyleManager.getInstance(file.getProject());
        styleManager.reformat(file);
        return file.getText();
    }
}
