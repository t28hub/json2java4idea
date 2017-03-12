package io.t28.json2java.idea.naming;

import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import com.intellij.testFramework.fixtures.JavaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import com.squareup.javapoet.TypeName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParameterNamePolicyTest {
    private JavaCodeInsightTestFixture fixture;

    private ParameterNamePolicy underTest;

    @Before
    public void setUp() throws Exception {
        final IdeaTestFixtureFactory ideaFixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        final JavaTestFixtureFactory javaFixtureFactory = JavaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> fixtureBuilder = ideaFixtureFactory.createLightFixtureBuilder();
        fixture = javaFixtureFactory.createCodeInsightFixture(fixtureBuilder.getFixture());
        fixture.setUp();

        final Project project = fixture.getProject();
        final JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(project);
        underTest = new ParameterNamePolicy(codeStyleManager);
    }

    @After
    public void tearDown() throws Exception {
        fixture.tearDown();
    }

    @Test
    public void convertShouldReturnLowerCamelText() throws Exception {
        // exercise
        final String actual = underTest.convert("Foo", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("foo");
    }

    @Test
    public void convertShouldAppendPrefixWhenRegistered() throws Exception {
        // exercise
        final String actual = underTest.convert("Class", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("aClass");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void convertShouldThrowExceptionWhenInvalidText() throws Exception {
        // verify
        assertThatThrownBy(() -> {
            // exercise
            underTest.convert("+", TypeName.OBJECT);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}