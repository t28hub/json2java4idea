package io.t28.json2java.idea.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.json2java.core.Configuration;
import io.t28.json2java.core.JavaConverter;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.naming.MethodNamePolicy;
import io.t28.json2java.idea.utils.GsonFormatter;
import io.t28.json2java.idea.validator.JsonValidator;
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.naming.ClassNamePolicy;
import io.t28.json2java.idea.naming.FieldNamePolicy;
import io.t28.json2java.idea.naming.ParameterNamePolicy;
import io.t28.json2java.idea.settings.Json2JavaSettings;
import io.t28.json2java.idea.utils.JsonFormatter;
import io.t28.json2java.idea.validator.NameValidator;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
public class PluginModule implements Module {
    public static final Annotation NAME_VALIDATOR_ANNOTATION = Names.named("Name");
    public static final Annotation JSON_VALIDATOR_ANNOTATION = Names.named("Json");

    private final AnActionEvent event;

    public PluginModule(@Nonnull AnActionEvent event) {
        this.event = event;
    }

    @Override
    public void configure(@Nonnull Binder binder) {
        // Binding AnActionEvent context related classes
        binder.bind(Project.class)
                .toInstance(event.getProject());
        binder.bind(IdeView.class)
                .toInstance(event.getData(LangDataKeys.IDE_VIEW));

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
        binder.bind(JsonFormatter.class)
                .to(GsonFormatter.class);

        // Installation factory modules
        binder.install(new FactoryModuleBuilder().build(CommandFactory.class));
    }

    @Nonnull
    @Provides
    @Singleton
    public Configuration provideConfiguration(@Nonnull Json2JavaSettings settings,
                                              @Nonnull @Named("ClassName") NamePolicy classNamePolicy,
                                              @Nonnull @Named("FieldName") NamePolicy fieldNamePolicy,
                                              @Nonnull @Named("MethodName") NamePolicy methodNamePolicy,
                                              @Nonnull @Named("ParameterName") NamePolicy parameterNamePolicy) {
        return Configuration.builder()
                .style(settings.getStyle())
                .classNamePolicy(classNamePolicy)
                .fieldNamePolicy(fieldNamePolicy)
                .methodNamePolicy(methodNamePolicy)
                .parameterNamePolicy(parameterNamePolicy)
                .build();
    }

    @Nonnull
    @Provides
    public JavaConverter provideJavaConverter(@Nonnull Configuration configuration) {
        return new JavaConverter(configuration);
    }

    @Nonnull
    @Provides
    @Singleton
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
