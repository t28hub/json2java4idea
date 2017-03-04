package io.t28.json2java.idea.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiceManager extends AbstractProjectComponent {
    private static final String NAME = GuiceManager.class.getSimpleName();

    @Nullable
    private Injector injector;

    public GuiceManager(@Nonnull Project project) {
        super(project);
    }

    @Nonnull
    public static GuiceManager getInstance(@Nonnull Project project) {
        return project.getComponent(GuiceManager.class);
    }

    @Override
    public void projectOpened() {
        injector = Guice.createInjector(new ProjectModule(getProject()));
    }

    @Override
    public void projectClosed() {
        injector = null;
    }

    @Nonnull
    @Override
    public String getComponentName() {
        return NAME;
    }

    @Nonnull
    public Injector getInjector() {
        if (injector == null) {
            throw new IllegalStateException("Injector has not been initialized yet, ot has been already disposed");
        }
        return injector;
    }

    @Nonnull
    private Project getProject() {
        return myProject;
    }
}
