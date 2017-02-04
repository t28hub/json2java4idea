package io.t28.pojojson.idea;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.PlatformIcons;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;

public class NewClassAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = event.getProject();
        if (project == null) {
            return;
        }

        final NewClassDialog dialog = new NewClassDialog(project);
        dialog.show();
        System.out.println(event);
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
}
