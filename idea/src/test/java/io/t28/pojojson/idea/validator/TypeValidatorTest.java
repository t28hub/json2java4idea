package io.t28.pojojson.idea.validator;

import io.t28.pojojson.idea.PluginBundle;
import io.t28.pojojson.idea.Type;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TypeValidatorTest {
    private PluginBundle bundle;

    private TypeValidator underTest;

    @Before
    public void setUp() throws Exception {
        bundle = spy(new PluginBundle());
        underTest = new TypeValidator(bundle);
    }

    @Test
    public void getErrorTextShouldReturnNullWhenTypeIsValid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText(Type.NONE.name());

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is null, but was <%s>", actual)
                .isNull();
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void getErrorTextShouldReturnTextWhenTypeIsInvalid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("unknown type");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is not empty, but was <%s>", actual)
                .isNotEmpty();
        verify(bundle).message(eq("error.message.validator.type.unsupported"));
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
    public void canCloseShouldReturnTrueWhenTypeIsValid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose(Type.NONE.name());

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is true, but was false")
                .isTrue();
    }

    @Test
    public void canCloseShouldReturnFalseWhenTypeIsInvalid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("unknown type");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }
}