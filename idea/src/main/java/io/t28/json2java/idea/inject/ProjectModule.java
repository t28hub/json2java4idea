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

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNameHelper;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import io.t28.json2java.core.naming.NamePolicy;
import io.t28.json2java.idea.Json2JavaBundle;
import io.t28.json2java.idea.command.CommandActionFactory;
import io.t28.json2java.idea.naming.ClassNamePolicy;
import io.t28.json2java.idea.naming.FieldNamePolicy;
import io.t28.json2java.idea.naming.MethodNamePolicy;
import io.t28.json2java.idea.naming.ParameterNamePolicy;
import io.t28.json2java.idea.setting.Json2JavaSettings;
import io.t28.json2java.idea.util.Formatter;
import io.t28.json2java.idea.validator.ClassPrefixValidator;
import io.t28.json2java.idea.validator.ClassSuffixValidator;
import io.t28.json2java.idea.validator.JsonValidator;
import io.t28.json2java.idea.validator.NameValidator;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
public class ProjectModule implements Module {
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
                .annotatedWith(Name.NAME_VALIDATOR.annotation())
                .to(NameValidator.class);
        binder.bind(InputValidator.class)
                .annotatedWith(Name.JSON_VALIDATOR.annotation())
                .to(JsonValidator.class);
        binder.bind(InputValidator.class)
                .annotatedWith(Name.CLASS_PREFIX_VALIDATOR.annotation())
                .to(ClassPrefixValidator.class);
        binder.bind(InputValidator.class)
                .annotatedWith(Name.CLASS_SUFFIX_VALIDATOR.annotation())
                .to(ClassSuffixValidator.class);

        // Binding NamePolicy classes
        binder.bind(NamePolicy.class)
                .annotatedWith(Name.CLASS_NAME_POLICY.annotation())
                .to(ClassNamePolicy.class);
        binder.bind(NamePolicy.class)
                .annotatedWith(Name.FIELD_NAME_POLICY.annotation())
                .to(FieldNamePolicy.class);
        binder.bind(NamePolicy.class)
                .annotatedWith(Name.METHOD_NAME_POLICY.annotation())
                .to(MethodNamePolicy.class);
        binder.bind(NamePolicy.class)
                .annotatedWith(Name.PARAMETER_NAME_POLICY.annotation())
                .to(ParameterNamePolicy.class);

        // Binding other classes
        binder.bind(Json2JavaBundle.class)
                .toInstance(Json2JavaBundle.getInstance());

        // Installation factory modules
        binder.install(new FactoryModuleBuilder().build(CommandActionFactory.class));
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

    @Nonnull
    @Provides
    @Named("Json")
    public Formatter provideJsonFormatter(@Nonnull PsiFileFactory fileFactory) {
        return new Formatter(fileFactory, JsonFileType.INSTANCE);
    }

    enum Name {
        NAME_VALIDATOR("Name"),
        JSON_VALIDATOR("Json"),
        CLASS_PREFIX_VALIDATOR("ClassPrefix"),
        CLASS_SUFFIX_VALIDATOR("ClassSuffix"),
        CLASS_NAME_POLICY("ClassName"),
        FIELD_NAME_POLICY("FieldName"),
        METHOD_NAME_POLICY("MethodName"),
        PARAMETER_NAME_POLICY("ParameterName");

        private final String name;

        Name(@Nonnull String name) {
            this.name = name;
        }

        @Nonnull
        Annotation annotation() {
            return Names.named(name);
        }
    }
}
