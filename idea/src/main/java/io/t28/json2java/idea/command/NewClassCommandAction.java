package io.t28.json2java.idea.command;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.idea.exceptions.ClassAlreadyExistsException;
import io.t28.json2java.idea.exceptions.ClassCreationException;
import io.t28.json2java.idea.exceptions.InvalidDirectoryException;
import io.t28.json2java.idea.utils.Extensions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.io.IOException;

public class NewClassCommandAction extends WriteCommandAction<PsiFile> {
    private final PsiFileFactory fileFactory;
    private final JavaDirectoryService directoryService;
    private final String name;
    private final String json;
    private final PsiDirectory directory;
    private final JavaConverter converter;

    @Inject
    public NewClassCommandAction(@Nonnull Project project,
                                 @Nonnull @Assisted("Name") String name,
                                 @Nonnull @Assisted("Json") String json,
                                 @Nonnull @Assisted PsiDirectory directory,
                                 @Nonnull @Assisted JavaConverter converter) {
        super(project);
        this.fileFactory = PsiFileFactory.getInstance(project);
        this.directoryService = JavaDirectoryService.getInstance();
        this.name = Preconditions.checkNotNull(name);
        this.json = Preconditions.checkNotNull(json);
        this.directory = Preconditions.checkNotNull(directory);
        this.converter = Preconditions.checkNotNull(converter);
    }

    @Override
    protected void run(@NotNull Result<PsiFile> result) throws Throwable {
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
            final PsiFile created = (PsiFile) directory.add(classFile);
            result.setResult(created);
        } catch (IOException e) {
            throw new ClassCreationException("Failed to create new class from JSON", e);
        }
    }
}
