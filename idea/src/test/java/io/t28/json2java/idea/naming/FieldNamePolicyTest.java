package io.t28.json2java.idea.naming;

import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FieldNamePolicyTest extends IdeaProjectTest {
    private FieldNamePolicy underTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        final JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(project);
        underTest = new FieldNamePolicy(codeStyleManager);
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
    public void convertShouldReturnTextWithPrefixWhenReserved() throws Exception {
        // exercise
        final String actual = underTest.convert("Private", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("aPrivate");
    }

    @Test
    public void convertShouldRemoveInvalidCharacter() throws Exception {
        // exercise
        final String actual = underTest.convert("1nva|id", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("a1nvaid");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void convertShouldThrowExceptionWhenInvalidName() throws Exception {
        assertThatThrownBy(() -> {
            // exercise
             underTest.convert("+-*/", TypeName.OBJECT);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}