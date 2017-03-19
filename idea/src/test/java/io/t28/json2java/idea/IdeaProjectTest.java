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
