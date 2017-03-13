package io.t28.json2java.idea;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import com.intellij.testFramework.fixtures.JavaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
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

    protected Project getProject() {
        if (fixture == null) {
            throw new AssertionError("fixture == null");
        }
        return fixture.getProject();
    }
}
