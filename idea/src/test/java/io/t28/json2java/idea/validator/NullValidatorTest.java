package io.t28.json2java.idea.validator;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NullValidatorTest {
    private NullValidator underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new NullValidator();
    }

    @Test
    public void checkInputShouldAlwaysReturnFalse() throws Exception {
        // exercise
        final boolean actual = underTest.checkInput("text");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by checkInput is false but was true")
                .isFalse();
    }

    @Test
    public void canCloseShouldAlwaysReturnFalse() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("text");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false but was true")
                .isFalse();
    }
}