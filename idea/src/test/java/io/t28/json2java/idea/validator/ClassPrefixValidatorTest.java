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

package io.t28.json2java.idea.validator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import io.t28.json2java.idea.IdeaProjectTest;
import io.t28.json2java.idea.Json2JavaBundle;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ClassPrefixValidatorTest extends IdeaProjectTest {
    private ClassPrefixValidator underTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        final Json2JavaBundle bundle = Json2JavaBundle.getInstance();
        final PsiNameHelper nameHelper = PsiNameHelper.getInstance(project);
        underTest = new ClassPrefixValidator(bundle, nameHelper);
    }

    @Test
    public void getErrorTextShouldReturnNullWhenValid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("Foo");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is null, but was <%s>", actual)
                .isNull();
    }

    @Test
    public void getErrorTextShouldReturnTextWhenInvalid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("^Foo");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is null, but was <%s>", actual)
                .isNotEmpty();
    }

    @Test
    public void checkInputShouldAlwaysReturnTrue() throws Exception {
        // exercise
        final boolean actual = underTest.checkInput("foo");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by checkInput is true, but was false")
                .isTrue();
    }

    @Test
    public void canCloseShouldReturnTrueWhenNull() throws Exception {
        // exercise
        final boolean actual = underTest.canClose(null);

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is true, but was false")
                .isTrue();
    }

    @Test
    public void canCloseShouldReturnTrueWhenValid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("Foo");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is true, but was false")
                .isTrue();
    }


    @Test
    public void canCloseShouldReturnFalseWhenReserved() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("private");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }

    @Test
    public void canCloseShouldReturnFalseWhenInvalid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("^invalid");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }
}