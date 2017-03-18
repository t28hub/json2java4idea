package io.t28.json2java.idea.utils;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FormatterTest extends IdeaProjectTest {
    private PsiFileFactory fileFactory;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        fileFactory = PsiFileFactory.getInstance(project);
    }

    @Test
    public void formatShouldReformatText() throws Exception {
        getApplication().invokeAndWait(() -> {
            // exercise
            final Formatter underTest = new Formatter(fileFactory, JsonFileType.INSTANCE);
            final String actual = underTest.format("{\"key\":\"value\",\"array\":[\"foo\",\"bar\"]}");

            // verify
            assertThat(actual)
                    .isEqualTo("{\n  \"key\": \"value\",\n  \"array\": [\n    \"foo\",\n    \"bar\"\n  ]\n}");
        });
    }
}