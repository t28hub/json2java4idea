package io.t28.json2java.idea;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import com.intellij.testFramework.fixtures.JavaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.swing.dependency.jsr305.Nonnull;
import org.junit.After;
import org.junit.Before;

public class IdeaProjectTest {
    private JavaCodeInsightTestFixture fixture;

    @Before
    public void setUp() throws Exception {
        final IdeaTestFixtureFactory ideaFixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        final JavaTestFixtureFactory javaFixtureFactory = JavaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> fixtureBuilder = ideaFixtureFactory.createLightFixtureBuilder();
        fixture = javaFixtureFactory.createCodeInsightFixture(fixtureBuilder.getFixture());
        fixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        fixture.tearDown();
    }

    @Nonnull
    @CheckReturnValue
    protected Application getApplication() {
        if (fixture == null) {
            throw new AssertionError("getApplication() must call after setUp()");
        }
        return ApplicationManager.getApplication();
    }

    @Nonnull
    @CheckReturnValue
    protected Project getProject() {
        if (fixture == null) {
            throw new AssertionError("getProject() must call after setUp()");
        }
        return fixture.getProject();
    }
}
