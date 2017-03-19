/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
