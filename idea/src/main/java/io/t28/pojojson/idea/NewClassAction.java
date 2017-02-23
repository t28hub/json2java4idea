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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.PlatformIcons;
import io.t28.pojojson.idea.commands.NewClassCommand;
import io.t28.pojojson.idea.exceptions.ClassAlreadyExistsException;
import io.t28.pojojson.idea.exceptions.ClassCreationException;
import io.t28.pojojson.idea.exceptions.InvalidDirectoryException;
import io.t28.pojojson.idea.exceptions.InvalidJsonException;
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
    private static final String NOTIFICATION_DISPLAY_ID = "Json2Java4Idea";

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

    @Inject
    @SuppressWarnings("unused")
    private PluginBundle bundle;

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

        final PsiDirectory selected = ideView.getOrChooseDirectory();
        if (selected == null) {
            final Notification notification = new Notification(
                    NOTIFICATION_DISPLAY_ID,
                    bundle.message("error.title.directory.unselected"),
                    bundle.message("error.message.directory.unselected"),
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
            return;
        }

        final NewClassDialog dialog = NewClassDialog.builder(project, bundle)
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
        CommandProcessor.getInstance().executeCommand(project, () -> {
            final PsiDirectory directory = ideView.getOrChooseDirectory();
            try {
                ApplicationManager.getApplication().runWriteAction(NewClassCommand.builder()
                        .directory(directory)
                        .directoryService(JavaDirectoryService.getInstance())
                        .fileFactory(PsiFileFactory.getInstance(project))
                        .className(dialog.getClassName())
                        .classStyle(dialog.getClassStyle())
                        .json(dialog.getJson())
                        .build()
                );
                dialog.close();
            } catch (InvalidDirectoryException e) {
                final Notification notification = new Notification(
                        NOTIFICATION_DISPLAY_ID,
                        bundle.message("error.title.directory.invalid"),
                        bundle.message("error.message.directory.invalid", directory),
                        NotificationType.WARNING
                );
                Notifications.Bus.notify(notification);
                dialog.close();
            } catch (ClassAlreadyExistsException e) {
                Messages.showMessageDialog(
                        project,
                        bundle.message("error.message.class.exists", dialog.getClassName()),
                        bundle.message("error.title.cannot.create.class"),
                        Messages.getErrorIcon()
                );
            } catch (ClassCreationException e) {
                final Notification notification = new Notification(
                        NOTIFICATION_DISPLAY_ID,
                        bundle.message("error.title.cannot.create.class"),
                        bundle.message("error.message.cannot.create", dialog.getClassName()),
                        NotificationType.ERROR
                );
                Notifications.Bus.notify(notification);
                dialog.close();
            }
        }, null, null);
    }

    @Override
    public void onCancel(@Nonnull NewClassDialog dialog) {
        dialog.cancel();
    }

    @Override
    public void onFormat(@Nonnull NewClassDialog dialog) {
        try {
            final String json = dialog.getJson();
            final JsonFormatter formatter = injector.getInstance(JsonFormatter.class);
            final String formatted = formatter.format(json);
            dialog.setJson(formatted);
        } catch (InvalidJsonException e) {
            final Notification notification = new Notification(
                    NOTIFICATION_DISPLAY_ID,
                    bundle.message("error.title.cannot.format.json"),
                    bundle.message("error.message.json.syntax"),
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
        } catch (IOException e) {
            final Notification notification = new Notification(
                    NOTIFICATION_DISPLAY_ID,
                    bundle.message("error.title.cannot.format.json"),
                    bundle.message("error.message.io"),
                    NotificationType.WARNING
            );
            Notifications.Bus.notify(notification, project);
        }
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
