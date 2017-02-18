package io.t28.pojojson.idea;

import com.intellij.ide.IdeView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.PlatformIcons;
import io.t28.pojojson.idea.commands.NewClassCommand;
import io.t28.pojojson.idea.exceptions.ClassCreationException;
import io.t28.pojojson.idea.ui.NewClassDialog;
import io.t28.pojojson.idea.utils.GsonFormatter;
import io.t28.pojojson.idea.utils.JsonFormatter;
import io.t28.pojojson.idea.validator.JsonValidator;
import io.t28.pojojson.idea.validator.NameValidator;
import io.t28.pojojson.idea.validator.TypeValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

import java.io.IOException;

public class NewClassAction extends AnAction implements NewClassDialog.ActionListener {
    private Project project;
    private IdeView ideView;
    private Application application;
    private CommandProcessor commandProcessor;
    private JsonFormatter formatter;

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        this.project = event.getProject();
        this.formatter = new GsonFormatter();
        this.application = ApplicationManager.getApplication();
        this.commandProcessor = CommandProcessor.getInstance();
        if (project == null) {
            throw new IllegalStateException("AnActionEvent.getProject() returns null");
        }

        ideView = event.getData(LangDataKeys.IDE_VIEW);
        if (ideView == null) {
            throw new IllegalStateException("Provided an instance of IdeView is null");
        }

        final PsiDirectory directory = ideView.getOrChooseDirectory();
        if (directory == null) {
            throw new IllegalStateException("Selected directory is null");
        }

        final NewClassDialog dialog = NewClassDialog.builder(project)
                .editorFactory(EditorFactory.getInstance())
                .nameValidator(new NameValidator(project))
                .typeValidator(new TypeValidator())
                .jsonValidator(new JsonValidator())
                .actionListener(this)
                .build();
        dialog.show();
    }

    @Override
    public void update(AnActionEvent event) {
        final IdeView view = event.getData(LangDataKeys.IDE_VIEW);
        if (view == null) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        final Project project = event.getData(LangDataKeys.PROJECT);
        if (project == null) {
            event.getPresentation().setEnabledAndVisible(false);
            return;
        }

        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
        final PsiDirectory[] directories = view.getDirectories();
        for (final PsiDirectory directory : directories) {
            final boolean isSourceRoot = fileIndex.isUnderSourceRootOfType(directory.getVirtualFile(), JavaModuleSourceRootTypes.SOURCES);
            if (isSourceRoot) {
                event.getPresentation().setIcon(PlatformIcons.CLASS_ICON);
                event.getPresentation().setEnabledAndVisible(true);
                return;
            }
        }
        event.getPresentation().setEnabledAndVisible(false);
    }

    @Override
    public void onOk(@NotNull NewClassDialog dialog) {
        commandProcessor.executeCommand(project, () -> {
            try {
                final PsiFile created = application.runWriteAction(NewClassCommand.builder()
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
    public void onCancel(@NotNull NewClassDialog dialog) {

    }

    @Override
    public void onFormat(@NotNull NewClassDialog dialog) {
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
}
