package io.t28.pojojson.idea.commands;

import com.google.common.base.Preconditions;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.pojojson.core.PojoJson;
import io.t28.pojojson.core.Style;
import io.t28.pojojson.idea.naming.IdeaFieldNamePolicy;
import io.t28.pojojson.idea.naming.IdeaMethodNamePolicy;
import io.t28.pojojson.idea.naming.IdeaParameterNamePolicy;
import io.t28.pojojson.idea.naming.IdeaClassNamePolicy;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.io.IOException;

public class NewClassCommand implements Runnable {
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
    public void run() {
        final PsiPackage packageElement = directoryService.getPackage(directory);
        if (packageElement == null) {
            throw new IllegalStateException("JavaDirectoryService returns null as a package");
        }

        final String packageName = packageElement.getQualifiedName();
        final Style style = Style.fromName(classStyle).orElse(Style.NONE);
        try {
            final Project project = directory.getProject();
            final String pojoClass = PojoJson.builder()
                    .style(style)
                    .classNamePolicy(new IdeaClassNamePolicy())
                    .methodNamePolicy(new IdeaMethodNamePolicy(project))
                    .fieldNamePolicy(new IdeaFieldNamePolicy(project))
                    .parameterNamePolicy(new IdeaParameterNamePolicy(project))
                    .build()
                    .generate(packageName, className, json);
            final PsiFile classFile = fileFactory.createFileFromText(className + ".java", JavaFileType.INSTANCE, pojoClass);
            CodeStyleManager.getInstance(classFile.getProject()).reformat(classFile);
            JavaCodeStyleManager.getInstance(classFile.getProject()).optimizeImports(classFile);
            directory.add(classFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate a POJO class from JSON", e);
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
        public Builder directory(PsiDirectory directory) {
            this.directory = directory;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder directoryService(JavaDirectoryService directoryService) {
            this.directoryService = directoryService;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder fileFactory(PsiFileFactory fileFactory) {
            this.fileFactory = fileFactory;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder className(String className) {
            this.className = className;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder classStyle(String classStyle) {
            this.classStyle = classStyle;
            return this;
        }

        @Nonnull
        @CheckReturnValue
        public Builder json(String json) {
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
