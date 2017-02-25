package io.t28.pojojson.idea.commands;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.json2java.core.JavaConverter;
import io.t28.pojojson.idea.exceptions.ClassAlreadyExistsException;
import io.t28.pojojson.idea.exceptions.ClassCreationException;
import io.t28.pojojson.idea.exceptions.InvalidDirectoryException;
import io.t28.pojojson.idea.utils.Extensions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class NewClassCommand implements ThrowableComputable<PsiFile, ClassCreationException> {
    private final String name;
    private final String json;
    private final PsiDirectory directory;
    private final JavaConverter converter;
    private final PsiFileFactory fileFactory;
    private final JavaDirectoryService directoryService;

    @Inject
    public NewClassCommand(@Nonnull @Assisted("Name") String name,
                           @Nonnull @Assisted("Json") String json,
                           @Nullable @Assisted PsiDirectory directory,
                           @Nonnull JavaConverter converter,
                           @Nonnull PsiFileFactory fileFactory,
                           @Nonnull JavaDirectoryService directoryService) {
        this.name = Preconditions.checkNotNull(name);
        this.json = Preconditions.checkNotNull(json);
        this.directory = Preconditions.checkNotNull(directory);
        this.converter = Preconditions.checkNotNull(converter);
        this.fileFactory = Preconditions.checkNotNull(fileFactory);
        this.directoryService = Preconditions.checkNotNull(directoryService);
    }

    @Nonnull
    @Override
    public PsiFile compute() throws ClassCreationException {
        final PsiPackage packageElement = directoryService.getPackage(directory);
        if (packageElement == null) {
            throw new InvalidDirectoryException("Target directory does not provide a package");
        }

        final String fileName = Extensions.append(name, StdFileTypes.JAVA);
        final PsiFile found = directory.findFile(fileName);
        if (found != null) {
            throw new ClassAlreadyExistsException("Class '" + name + "'already exists in " + packageElement.getName());
        }

        final String packageName = packageElement.getQualifiedName();
        final String className = Extensions.remove(this.name, StdFileTypes.JAVA);
        try {
            final String java = converter.convert(packageName, className, json);
            final PsiFile classFile = fileFactory.createFileFromText(fileName, JavaFileType.INSTANCE, java);
            CodeStyleManager.getInstance(classFile.getProject()).reformat(classFile);
            JavaCodeStyleManager.getInstance(classFile.getProject()).optimizeImports(classFile);
            return (PsiFile) directory.add(classFile);
        } catch (IOException e) {
            throw new ClassCreationException("Failed to create new class from JSON", e);
        }
    }
}
