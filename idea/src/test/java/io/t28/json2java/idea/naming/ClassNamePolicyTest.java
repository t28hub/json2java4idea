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

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ClassNamePolicyTest extends IdeaProjectTest {
    private PsiNameHelper nameHelper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        nameHelper = PsiNameHelper.getInstance(project);
    }

    @Test
    public void convertShouldReturnUpperCamelText() throws Exception {
        // exercise
        final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "", "");
        final String actual = underTest.convert("foo_bar", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("FooBar");
    }

    @Test
    public void convertShouldReturnTextWithPrefix() throws Exception {
        // exercise
        final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "Baz", "");
        final String actual = underTest.convert("foo_bar", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("BazFooBar");
    }

    @Test
    public void convertShouldReturnTextWithSuffix() throws Exception {
        // exercise
        final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "", "Baz");
        final String actual = underTest.convert("foo_bar", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("FooBarBaz");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void convertShouldThrowExceptionWhenUnQualifiedName() throws Exception {
        // verify
        assertThatThrownBy(() -> {
            // exercise
            final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "", "");
            underTest.convert("+1", TypeName.OBJECT);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}