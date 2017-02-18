package io.t28.pojojson.idea;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.PlatformIcons;
import io.t28.pojojson.idea.commands.NewClassCommand;
import io.t28.pojojson.idea.exceptions.ClassCreationException;
import io.t28.pojojson.idea.inject.ActionModule;
import io.t28.pojojson.idea.ui.NewClassDialog;
import io.t28.pojojson.idea.utils.JsonFormatter;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class NewClassAction extends AnAction implements NewClassDialog.ActionListener {
    private static final Key<InputValidator> NAME_VALIDATOR_KEY = Key.get(
            InputValidator.class, ActionModule.NAME_VALIDATOR_ANNOTATION
    );

    private static final Key<InputValidator> TYPE_VALIDATOR_KEY = Key.get(
            InputValidator.class, ActionModule.TYPE_VALIDATOR_ANNOTATION
    );

    private static final Key<InputValidator> JSON_VALIDATOR_KEY = Key.get(
            InputValidator.class, ActionModule.JSON_VALIDATOR_ANNOTATION
    );

    private Injector injector;

    @Inject
    @SuppressWarnings("unused")
    private Project project;

    @Inject
    @SuppressWarnings("unused")
    private IdeView ideView;

    public NewClassAction() {
        super(PlatformIcons.CLASS_ICON);
    }

    @Override
    public void actionPerformed(@Nonnull AnActionEvent event) {
        if (!isAvailable(event)) {
            return;
        }

        injector = Guice.createInjector(new ActionModule(event));
        injector.injectMembers(this);
        if (!isValidDirectory()) {
            final Notification notification = new Notification(
                    "POJO.json",
                    "Invalid target directory",
                    "Cannot create a class in the selected directory",
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
            return;
        }

        final NewClassDialog dialog = NewClassDialog.builder(project)
                .editorFactory(injector.getInstance(EditorFactory.class))
                .nameValidator(injector.getInstance(NAME_VALIDATOR_KEY))
                .typeValidator(injector.getInstance(TYPE_VALIDATOR_KEY))
                .jsonValidator(injector.getInstance(JSON_VALIDATOR_KEY))
                .actionListener(this)
                .build();
        dialog.show();
    }

    @Override
    public void update(@Nonnull AnActionEvent event) {
        final boolean isAvailable = isAvailable(event);
        final Presentation presentation = event.getPresentation();
        presentation.setEnabledAndVisible(isAvailable);
    }

    @Override
    public void onOk(@Nonnull NewClassDialog dialog) {
        final CommandProcessor processor = injector.getInstance(CommandProcessor.class);
        final Application application = injector.getInstance(Application.class);
        processor.executeCommand(project, () -> {
            try {
                application.runWriteAction(NewClassCommand.builder()
                        .directory(ideView.getOrChooseDirectory())
                        .directoryService(JavaDirectoryService.getInstance())
                        .fileFactory(PsiFileFactory.getInstance(project))
                        .className(dialog.getClassName())
                        .classStyle(dialog.getClassStyle())
                        .json(dialog.getJson())
                        .build());
            } catch (ClassCreationException e) {
                e.printStackTrace();
                final Notification notification = new Notification(
                        "POJO.json",
                        "Cannot create class",
                        "Failed to create class " + dialog.getClassName(),
                        NotificationType.WARNING
                );
                Notifications.Bus.notify(notification, project);
            }
        }, null, null);
    }

    @Override
    public void onCancel(@Nonnull NewClassDialog dialog) {
    }

    @Override
    public void onFormat(@Nonnull NewClassDialog dialog) {
        final JsonFormatter formatter = injector.getInstance(JsonFormatter.class);
        try {
            final String json = dialog.getJson();
            final String formatted = formatter.format(json);
            dialog.setJson(formatted);
        } catch (IOException e) {
            final Notification notification = new Notification(
                    "POJO.json",
                    "I/O error",
                    "An I/O error occurred",
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
        } catch (IllegalArgumentException e) {
            final Notification notification = new Notification(
                    "POJO.json",
                    "Syntax error",
                    "Specified JSON has syntax error",
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
        }
    }

    @CheckReturnValue
    private boolean isValidDirectory() {
        final PsiDirectory targetDirectory = ideView.getOrChooseDirectory();
        if (targetDirectory == null) {
            return false;
        }

        final JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
        final PsiPackage targetPackage = directoryService.getPackage(targetDirectory);
        return targetPackage != null;
    }

    @CheckReturnValue
    @VisibleForTesting
    @SuppressWarnings("WeakerAccess")
    static boolean isAvailable(@Nonnull AnActionEvent event) {
        final Project project = event.getProject();
        if (project == null) {
            return false;
        }

        final IdeView view = event.getData(LangDataKeys.IDE_VIEW);
        if (view == null) {
            return false;
        }

        final ProjectRootManager rootManager = ProjectRootManager.getInstance(project);
        final ProjectFileIndex fileIndex = rootManager.getFileIndex();
        final Optional<PsiDirectory> sourceDirectory = Stream.of(view.getDirectories())
                .filter(directory -> {
                    final VirtualFile virtualFile = directory.getVirtualFile();
                    return fileIndex.isUnderSourceRootOfType(virtualFile, JavaModuleSourceRootTypes.SOURCES);
                })
                .findFirst();
        return sourceDirectory.isPresent();
    }
}
