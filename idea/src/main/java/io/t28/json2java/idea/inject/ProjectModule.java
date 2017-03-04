package io.t28.json2java.idea.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.naming.ClassNamePolicy;
import io.t28.json2java.idea.naming.FieldNamePolicy;
import io.t28.json2java.idea.naming.MethodNamePolicy;
import io.t28.json2java.idea.naming.ParameterNamePolicy;
import io.t28.json2java.idea.settings.Json2JavaSettings;
import io.t28.json2java.idea.validator.JsonValidator;
import io.t28.json2java.idea.validator.NameValidator;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
public class ProjectModule implements Module {
    public static final Annotation NAME_VALIDATOR_ANNOTATION = Names.named("Name");
    public static final Annotation JSON_VALIDATOR_ANNOTATION = Names.named("Json");

    private final Project project;

    public ProjectModule(@Nonnull Project project) {
        this.project = project;
    }

    @Override
    public void configure(@Nonnull Binder binder) {
        binder.bind(Project.class)
                .toInstance(project);

        // Binding InputValidator related classes
        binder.bind(InputValidator.class)
                .annotatedWith(NAME_VALIDATOR_ANNOTATION)
                .to(NameValidator.class);
        binder.bind(InputValidator.class)
                .annotatedWith(JSON_VALIDATOR_ANNOTATION)
                .to(JsonValidator.class);

        // Binding NamePolicy classes
        binder.bind(NamePolicy.class)
                .annotatedWith(Names.named("ClassName"))
                .to(ClassNamePolicy.class);
        binder.bind(NamePolicy.class)
                .annotatedWith(Names.named("FieldName"))
                .to(FieldNamePolicy.class);
        binder.bind(NamePolicy.class)
                .annotatedWith(Names.named("MethodName"))
                .to(MethodNamePolicy.class);
        binder.bind(NamePolicy.class)
                .annotatedWith(Names.named("ParameterName"))
                .to(ParameterNamePolicy.class);

        // Binding other classes
        binder.bind(Json2JavaBundle.class)
                .toInstance(Json2JavaBundle.getInstance());

        // Installation factory modules
        binder.install(new FactoryModuleBuilder().build(CommandFactory.class));
    }

    @Nonnull
    @Provides
    public Json2JavaSettings provideSettings(@Nonnull Project project) {
        return Json2JavaSettings.getInstance(project);
    }

    @Nonnull
    @Provides
    @Singleton
    public JavaCodeStyleManager provideCodeStyleManager(@Nonnull Project project) {
        return JavaCodeStyleManager.getInstance(project);
    }

    @Nonnull
    @Provides
    @Singleton
    public JavaDirectoryService provideDirectoryService() {
        return JavaDirectoryService.getInstance();
    }

    @Nonnull
    @Provides
    @Singleton
    public PsiManager provideManager(@Nonnull Project project) {
        return PsiManager.getInstance(project);
    }

    @Nonnull
    @Provides
    @Singleton
    public PsiNameHelper provideNameHelper(@Nonnull Project project) {
        return PsiNameHelper.getInstance(project);
    }

    @Nonnull
    @Provides
    @Singleton
    public PsiFileFactory provideFileFactory(@Nonnull Project project) {
        return PsiFileFactory.getInstance(project);
    }
}
