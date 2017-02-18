package io.t28.pojojson.idea.inject;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.psi.PsiNameHelper;
import io.t28.pojojson.idea.utils.GsonFormatter;
import io.t28.pojojson.idea.utils.JsonFormatter;
import io.t28.pojojson.idea.validator.JsonValidator;
import io.t28.pojojson.idea.validator.NameValidator;
import io.t28.pojojson.idea.validator.TypeValidator;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
public class ActionModule implements Module {
    public static final Annotation NAME_VALIDATOR_ANNOTATION = Names.named("Name");
    public static final Annotation TYPE_VALIDATOR_ANNOTATION = Names.named("Type");
    public static final Annotation JSON_VALIDATOR_ANNOTATION = Names.named("Json");

    private final AnActionEvent event;

    public ActionModule(@Nonnull AnActionEvent event) {
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
                .annotatedWith(TYPE_VALIDATOR_ANNOTATION)
                .to(TypeValidator.class);
        binder.bind(InputValidator.class)
                .annotatedWith(JSON_VALIDATOR_ANNOTATION)
                .to(JsonValidator.class);

        // Binding other classes
        binder.bind(JsonFormatter.class)
                .to(GsonFormatter.class);
    }

    @Nonnull
    @Provides
    @Singleton
    public Application provideApplication() {
        return ApplicationManager.getApplication();
    }

    @Nonnull
    @Provides
    @Singleton
    public CommandProcessor provideCommandProcessor() {
        return CommandProcessor.getInstance();
    }

    @Nonnull
    @Provides
    @Singleton
    public EditorFactory provideEditorFactory() {
        return EditorFactory.getInstance();
    }

    @Nonnull
    @Provides
    @Singleton
    public PsiNameHelper provideNameHelper(@Nonnull Project project) {
        return PsiNameHelper.getInstance(project);
    }
}
