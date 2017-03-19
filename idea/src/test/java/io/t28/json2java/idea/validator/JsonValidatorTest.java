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

import com.google.gson.JsonParser;
import io.t28.json2java.idea.Json2JavaBundle;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class JsonValidatorTest {
    private Json2JavaBundle bundle;

    private JsonValidator underTest;

    @Before
    public void setUp() throws Exception {
        bundle = spy(new Json2JavaBundle());
        underTest = new JsonValidator(bundle, new JsonParser());
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void getErrorTextShouldReturnTextWhenJsonIsEmpty() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is not empty, but was <%s>", actual)
                .isNotEmpty();

        verify(bundle).message("error.message.validator.json.empty");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void getErrorTextShouldReturnTextWhenJsonIsPrimitive() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("1000");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is not empty, but was <%s>", actual)
                .isNotEmpty();

        verify(bundle).message("error.message.validator.json.primitive");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void getErrorTextShouldReturnTextWhenJsonIsInvalid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("{{}}");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is not empty, but was <%s>", actual)
                .isNotEmpty();

        verify(bundle).message("error.message.validator.json.invalid");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void getErrorTextShouldReturnNullWhenJsonIsValid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("{}");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is null, but was <%s>", actual)
                .isNull();
    }

    @Test
    public void checkInputShouldAlwaysReturnTrue() throws Exception {
        // exercise
        final boolean actual = underTest.checkInput("test");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by checkInput is true, but was false")
                .isTrue();
    }

    @Test
    public void canCloseShouldReturnFalseWhenJsonIsEmpty() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }

    @Test
    public void canCloseShouldReturnFalseWhenJsonIsPrimitive() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("1000");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }

    @Test
    public void canCloseShouldReturnFalseWhenJsonIsInvalid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("{{}}");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }

    @Test
    public void canCloseShouldReturnTrueWhenJsonIsValid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("{}");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is true, but was false")
                .isTrue();
    }
}