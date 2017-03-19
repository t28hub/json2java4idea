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

package io.t28.json2java.idea.naming;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.idea.IdeaProjectTest;
import io.t28.json2java.idea.util.PsiTypeConverter;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MethodNamePolicyTest extends IdeaProjectTest {
    private Application application;

    private MethodNamePolicy underTest;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        application = getApplication();

        final Project project = getProject();
        final PsiManager psiManager = PsiManager.getInstance(project);
        final PsiTypeConverter converter = new PsiTypeConverter(psiManager);
        underTest = new MethodNamePolicy(project, converter);
    }

    @Test
    public void convertShouldReturnLowerCamelTextWithPrefix() throws Exception {
        application.invokeAndWait(() -> {
            // exercise
            final String actual = underTest.convert("Foo_Bar", TypeName.OBJECT);

            // verify
            assertThat(actual)
                    .isEqualTo("getFooBar");
        });
    }

    @Test
    public void convertShouldReturnWithPrefixWhenBoolean() throws Exception {
        application.invokeAndWait(() -> {
            // exercise
            final String actual = underTest.convert("Foo_Bar", TypeName.BOOLEAN);

            // verify
            assertThat(actual)
                    .isEqualTo("isFooBar");
        });
    }

    @Test
    public void convertShouldRemoveInvalidCharacters() throws Exception {
        application.invokeAndWait(() -> {
            // exercise
            final String actual = underTest.convert("/*Foo_Bar*/", TypeName.OBJECT);

            // verify
            assertThat(actual)
                    .isEqualTo("getFooBar");
        });
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void convertShouldThrowExceptionWhenInvalidName() throws Exception {
        application.invokeAndWait(() -> {
            // verify
            assertThatThrownBy(() -> {
                // exercise
                underTest.convert("+-*/", TypeName.OBJECT);
            }).isInstanceOf(IllegalArgumentException.class);
        });
    }
}
