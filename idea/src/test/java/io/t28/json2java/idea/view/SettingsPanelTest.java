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

package io.t28.json2java.idea.view;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.testFramework.fixtures.BareTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import io.t28.json2java.core.Style;
import io.t28.json2java.idea.Json2JavaBundle;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.swing.dependency.jsr305.Nonnull;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.Containers;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JPanelFixture;
import org.assertj.swing.fixture.JRadioButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.jetbrains.annotations.PropertyKey;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Ignore
public class SettingsPanelTest {
    private static Json2JavaBundle bundle;

    private BareTestFixture testFixture;
    private Application application;
    private FrameFixture window;

    private SettingsPanel underTest;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        bundle = Json2JavaBundle.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        final IdeaTestFixtureFactory ideaFixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        testFixture = ideaFixtureFactory.createBareFixture();
        testFixture.setUp();
        application = ApplicationManager.getApplication();
        underTest = GuiActionRunner.execute(SettingsPanel::new);
        window = Containers.showInFrame(underTest.getComponent());
    }

    @After
    public void tearDown() throws Exception {
        application.invokeAndWait(() -> underTest.dispose());
        window.cleanUp();
        testFixture.tearDown();
    }

    @Test
    public void disposeShouldReleaseEditor() throws Exception {
        // exercise
        application.invokeAndWait(() -> underTest.dispose());

        // verify
        final Editor[] actual = EditorFactory.getInstance().getAllEditors();
        assertThat(actual)
                .isEmpty();
    }

    @Test
    public void actionPerformedShouldCallOnChanged() throws Exception {
        // setup
        final SettingsPanel underTest = spy(this.underTest);

        // exercise
        final ActionEvent event = mock(ActionEvent.class);
        underTest.actionPerformed(event);

        // verify
        verify(underTest).onChanged();
    }

    @Test
    public void getStyleShouldReturnNoneByDefault() throws Exception {
        // exercise
        final Style actual = underTest.getStyle();

        // verify
        assertThat(actual).isEqualTo(Style.NONE);
    }

    @Test
    public void getStyleShouldReturnSelectedStyle() throws Exception {
        // setup
        final JRadioButtonFixture fixture = findRadioButtonByName("settings.name.style.gson");
        fixture.requireEnabled();
        fixture.requireNotSelected();
        fixture.target().setSelected(true);

        // exercise
        final Style actual = underTest.getStyle();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected style to be <%s> but was <%s>", Style.GSON, actual)
                .isEqualTo(Style.GSON);
    }

    @Test
    public void setStyleShouldSelectButton() throws Exception {
        // setup
        final JRadioButtonFixture fixture = findRadioButtonByName("settings.name.style.moshi");
        fixture.requireEnabled();
        fixture.requireNotSelected();

        // exercise
        underTest.setStyle(Style.MOSHI);

        // verify
        final JRadioButton radioButton = fixture.target();
        assertThat(radioButton.isSelected()).isTrue();
    }

    @Test
    public void getClassNamePrefixShouldReturnEmptyTextByDefault() throws Exception {
        // setup
        final JTextComponentFixture fixture = findTextFieldByName("settings.name.class.prefix");
        fixture.requireEnabled();
        fixture.requireEmpty();

        // exercise
        final String actual = underTest.getClassNamePrefix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected prefix to be empty but was <%s>", actual)
                .isEmpty();
    }

    @Test
    public void getClassNamePrefixShouldReturnText() throws Exception {
        // setup
        final JTextComponentFixture fixture = findTextFieldByName("settings.name.class.prefix");
        fixture.requireEnabled();
        fixture.requireEmpty();
        fixture.target().setText("Foo");

        // exercise
        final String actual = underTest.getClassNamePrefix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected prefix to be <%s> but was <%s>", "Foo", actual)
                .isEqualTo("Foo");
    }

    @Test
    public void setClassNamePrefixShouldSetText() throws Exception {
        // setup
        final JTextComponentFixture fixture = findTextFieldByName("settings.name.class.prefix");
        fixture.requireEnabled();
        fixture.requireEmpty();

        // exercise
        underTest.setClassNamePrefix("Foo");

        // verify
        final String actual = fixture.text();
        assertThat(actual)
                .overridingErrorMessage("Expected prefix to be <%s> but was <%s>", "Foo", actual)
                .isEqualTo("Foo");
    }

    @Test
    public void getClassNameSuffixShouldReturnEmptyTextByDefault() throws Exception {
        // setup
        final JTextComponentFixture fixture = findTextFieldByName("settings.name.class.suffix");
        fixture.requireEnabled();
        fixture.requireEmpty();

        // exercise
        final String actual = underTest.getClassNameSuffix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected suffix to be empty but was <%s>", actual)
                .isEmpty();
    }

    @Test
    public void getClassNameSuffixShouldReturnText() throws Exception {
        // setup
        final JTextComponentFixture fixture = findTextFieldByName("settings.name.class.suffix");
        fixture.requireEnabled();
        fixture.requireEmpty();
        fixture.target().setText("Bar");

        // exercise
        final String actual = underTest.getClassNameSuffix();

        // verify
        assertThat(actual)
                .overridingErrorMessage("Expected suffix to be <%s> but was <%s>", "Bar", actual)
                .isEqualTo("Bar");
    }

    @Test
    public void setClassNameSuffixShouldSetText() throws Exception {
        // setup
        final JTextComponentFixture fixture = findTextFieldByName("settings.name.class.suffix");
        fixture.requireEnabled();
        fixture.requireEmpty();

        // exercise
        underTest.setClassNameSuffix("Bar");

        // verify
        final String actual = fixture.target().getText();
        assertThat(actual)
                .overridingErrorMessage("Expected suffix to be <%s> but was <%s>", "Foo", actual)
                .isEqualTo("Bar");
    }

    @Test
    public void setPreviewTextShouldReplaceText() throws Exception {
        // setup
        final JPanelFixture fixture = findPreviewPanel();
        fixture.requireEnabled();

        // exercise
        final String text = "package io.t28.test;\n\nclass Foo {\n}\n";
        application.invokeAndWait(() -> underTest.setPreviewText(text));

        // verify
        final String actual = underTest.getPreviewText();
        assertThat(actual)
                .overridingErrorMessage("Expected preview text to be <%s> but was <%s>", text, actual)
                .isEqualTo(text);
    }

    @Nonnull
    @CheckReturnValue
    private JPanelFixture findPreviewPanel() {
        return window.panel("preview");
    }

    @Nonnull
    @CheckReturnValue
    private JRadioButtonFixture findRadioButtonByName(@Nonnull @PropertyKey(resourceBundle = "messages.Json2JavaBundle") String key) {
        return window.radioButton(bundle.message(key));
    }

    @Nonnull
    @CheckReturnValue
    private JTextComponentFixture findTextFieldByName(@Nonnull @PropertyKey(resourceBundle = "messages.Json2JavaBundle") String key) {
        return window.textBox(bundle.message(key));
    }
}