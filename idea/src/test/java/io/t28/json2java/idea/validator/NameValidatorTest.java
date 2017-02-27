package io.t28.json2java.idea.validator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import com.intellij.util.ThrowableRunnable;
import com.intellij.util.ui.UIUtil;
import io.t28.json2java.idea.Json2JavaBundle;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameValidatorTest {
    private static final TestFixtureBuilder<IdeaProjectTestFixture> PRIJECT_BUILDER = IdeaTestFixtureFactory
            .getFixtureFactory()
            .createFixtureBuilder(NameValidator.class.getName());

    private static IdeaProjectTestFixture fixture;

    private Json2JavaBundle bundle;

    private NameValidator underTest;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        fixture = PRIJECT_BUILDER.getFixture();
        fixture.setUp();
    }

    @AfterClass
    public static void tearDown() throws Throwable {
        UIUtil.invokeAndWaitIfNeeded((ThrowableRunnable) () -> fixture.tearDown());
    }

    @Before
    public void setUp() throws Exception {
        final Project project = fixture.getProject();
        bundle = spy(new Json2JavaBundle());
        underTest = new NameValidator(bundle, PsiNameHelper.getInstance(project));
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void getErrorTextShouldReturnTextWhenNameIsInvalid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("private");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by getErrorText is not empty, but was <%s>", actual)
                .isNotEmpty();

        verify(bundle).message(eq("error.message.validator.name.invalid"));
    }

    @Test
    public void getErrorTextShouldReturnNullWhenNameIsValid() throws Exception {
        // exercise
        final String actual = underTest.getErrorText("Test");

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
    public void canCloseShouldReturnTrueWhenNameIsValid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("Test");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is true, but was false")
                .isTrue();
    }

    @Test
    public void canCloseShouldReturnFalseWhenNameIsInvalid() throws Exception {
        // exercise
        final boolean actual = underTest.canClose("private");

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected value returned by canClose is false, but was true")
                .isFalse();
    }
}