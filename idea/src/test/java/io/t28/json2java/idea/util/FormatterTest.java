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

import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FormatterTest extends IdeaProjectTest {
    private PsiFileFactory fileFactory;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        fileFactory = PsiFileFactory.getInstance(project);
    }

    @Test
    public void formatShouldReformatText() throws Exception {
        getApplication().invokeAndWait(() -> {
            // exercise
            final Formatter underTest = new Formatter(fileFactory, JsonFileType.INSTANCE);
            final String actual = underTest.format("{\"key\":\"value\",\"array\":[\"foo\",\"bar\"]}");

            // verify
            assertThat(actual)
                    .isEqualTo("{\n  \"key\": \"value\",\n  \"array\": [\n    \"foo\",\n    \"bar\"\n  ]\n}");
        });
    }
}