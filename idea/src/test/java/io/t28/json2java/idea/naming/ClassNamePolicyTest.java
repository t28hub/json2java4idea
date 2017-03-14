package io.t28.json2java.idea.naming;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiNameHelper;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ClassNamePolicyTest extends IdeaProjectTest {
    private PsiNameHelper nameHelper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        nameHelper = PsiNameHelper.getInstance(project);
    }

    @Test
    public void convertShouldReturnUpperCamelText() throws Exception {
        // exercise
        final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "", "");
        final String actual = underTest.convert("foo_bar", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("FooBar");
    }

    @Test
    public void convertShouldReturnTextWithPrefix() throws Exception {
        // exercise
        final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "Baz", "");
        final String actual = underTest.convert("foo_bar", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("BazFooBar");
    }

    @Test
    public void convertShouldReturnTextWithSuffix() throws Exception {
        // exercise
        final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "", "Baz");
        final String actual = underTest.convert("foo_bar", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("FooBarBaz");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void convertShouldThrowExceptionWhenUnQualifiedName() throws Exception {
        // verify
        assertThatThrownBy(() -> {
            // exercise
            final ClassNamePolicy underTest = new ClassNamePolicy(nameHelper, "", "");
            underTest.convert("+1", TypeName.OBJECT);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}