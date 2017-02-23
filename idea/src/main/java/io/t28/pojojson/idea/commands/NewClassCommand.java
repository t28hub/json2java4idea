package io.t28.pojojson.idea.commands;

import com.google.common.base.Preconditions;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.pojojson.core.PojoJson;
import io.t28.pojojson.core.Style;
import io.t28.pojojson.idea.exceptions.ClassAlreadyExistsException;
import io.t28.pojojson.idea.exceptions.ClassCreationException;
import io.t28.pojojson.idea.exceptions.InvalidDirectoryException;
import io.t28.pojojson.idea.naming.ClassNamePolicy;
import io.t28.pojojson.idea.naming.FieldNamePolicy;
import io.t28.pojojson.idea.naming.MethodNamePolicy;
import io.t28.pojojson.idea.naming.ParameterNamePolicy;
import io.t28.pojojson.idea.utils.Extensions;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class NewClassCommand implements ThrowableComputable<PsiFile, ClassCreationException> {
    private final PsiDirectory directory;
    private final JavaDirectoryService directoryService;
    private final PsiFileFactory fileFactory;
    private final String className;
    private final String classStyle;
    private final String json;

    private NewClassCommand(@Nonnull Builder builder) {
        directory = Preconditions.checkNotNull(builder.directory);
        directoryService = Preconditions.checkNotNull(builder.directoryService);
        fileFactory = Preconditions.checkNotNull(builder.fileFactory);
        className = Preconditions.checkNotNull(builder.className);
        classStyle = Preconditions.checkNotNull(builder.classStyle);
        json = Preconditions.checkNotNull(builder.json);
    }

    @Nonnull
    @CheckReturnValue
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public PsiFile compute() throws ClassCreationException {
        final PsiPackage packageElement = directoryService.getPackage(directory);
        if (packageElement == null) {
            throw new InvalidDirectoryException("Target directory does not provide a package");
        }

        final String fileName = Extensions.append(className, StdFileTypes.JAVA);
        final PsiFile found = directory.findFile(fileName);
        if (found != null) {
            throw new ClassAlreadyExistsException("Class '" + className + "'already exists in " + packageElement.getName());
        }

        final String packageName = packageElement.getQualifiedName();
        final String className = Extensions.remove(this.className, StdFileTypes.JAVA);
        final Style style = Style.fromName(classStyle).orElse(Style.NONE);
        try {
            final Project project = directory.getProject();
            final String pojoClass = PojoJson.builder()
                    .style(style)
                    .classNamePolicy(new ClassNamePolicy())
                    .methodNamePolicy(new MethodNamePolicy(project))
                    .fieldNamePolicy(new FieldNamePolicy(project))
                    .parameterNamePolicy(new ParameterNamePolicy(project))
                    .build()
                    .generate(packageName, className, json);
            final PsiFile classFile = fileFactory.createFileFromText(fileName, JavaFileType.INSTANCE, pojoClass);
            CodeStyleManager.getInstance(classFile.getProject()).reformat(classFile);
            JavaCodeStyleManager.getInstance(classFile.getProject()).optimizeImports(classFile);
            return (PsiFile) directory.add(classFile);
        } catch (IOException e) {
            throw new ClassCreationException("Failed to create new class from JSON", e);
        }
    }

    public static class Builder {
        private PsiDirectory directory;
        private JavaDirectoryService directoryService;
        private PsiFileFactory fileFactory;
        private String className;
        private String classStyle;
        private String json;

        private Builder() {
        }

        @Nonnull
        @CheckReturnValue
        public Builder directory(@Nullable PsiDirectory directory) {
            this.directory = directory;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder directoryService(@Nonnull JavaDirectoryService directoryService) {
            this.directoryService = directoryService;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder fileFactory(@Nonnull PsiFileFactory fileFactory) {
            this.fileFactory = fileFactory;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder className(@Nonnull String className) {
            this.className = className;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder classStyle(@Nonnull String classStyle) {
            this.classStyle = classStyle;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder json(@Nonnull String json) {
            this.json = json;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public NewClassCommand build() {
            return new NewClassCommand(this);
        }
    }
}
