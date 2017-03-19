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

package io.t28.json2java.idea.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PsiTypeConverterTest extends IdeaProjectTest {
    @Parameters(name = "should return {1} when {0}")
    public static Collection<Object[]> fixtures() {
        return Arrays.asList(new Object[][]{
                {TypeName.BOOLEAN, PsiType.BOOLEAN},
                {TypeName.BYTE, PsiType.BYTE},
                {TypeName.CHAR, PsiType.CHAR},
                {TypeName.DOUBLE, PsiType.DOUBLE},
                {TypeName.FLOAT, PsiType.FLOAT},
                {TypeName.INT, PsiType.INT},
                {TypeName.LONG, PsiType.LONG},
                {TypeName.SHORT, PsiType.SHORT},
                {TypeName.VOID, PsiType.VOID},
        });
    }

    private final TypeName typeName;

    private final PsiType expected;

    private PsiTypeConverter underTest;

    public PsiTypeConverterTest(@Nonnull TypeName typeName, @Nonnull PsiType expected) {
        this.typeName = typeName;
        this.expected = expected;
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        final PsiManager psiManager = PsiManager.getInstance(project);
        underTest = new PsiTypeConverter(psiManager);
    }

    @Test
    public void apply() throws Exception {
        // exercise
        final PsiType actual = underTest.apply(typeName);

        // verify
        assertThat(actual)
                .isEqualTo(expected);
    }
}