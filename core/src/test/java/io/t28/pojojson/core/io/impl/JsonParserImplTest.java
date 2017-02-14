package io.t28.pojojson.core.io.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import io.t28.pojojson.core.io.exception.JsonParseException;
import io.t28.pojojson.core.json.JsonValue;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.t28.pojojson.core.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class JsonParserImplTest {
    private ObjectMapper mapper;
    private JsonParserImpl underTest;

    @Before
    public void setUp() throws Exception {
        mapper = spy(new ObjectMapper());
        underTest = new JsonParserImpl(mapper);
    }

    @Test
    public void parseShouldReturnJsonNullWhenNull() throws Exception {
        // exercise
        final JsonValue value = underTest.parse("null");

        // verify
        assertThat(value)
                .isJsonNull()
                .hasType(TypeName.OBJECT)
                .hasValue(null);
    }

    @Test
    public void parseShouldReturnJsonNumberWhenInt() throws Exception {
        // exercise
        final JsonValue value = underTest.parse("1024");

        // verify
        assertThat(value)
                .isJsonNumber()
                .hasType(TypeName.INT)
                .hasValue(1024);
    }

    @Test
    public void parseShouldReturnJsonNumberWhenLong() throws Exception {
        // exercise
        final JsonValue value = underTest.parse("4294967296");

        // verify
        assertThat(value)
                .isJsonNumber()
                .hasType(TypeName.LONG)
                .hasValue(4294967296L);
    }

    @Test
    public void parseShouldReturnJsonStringWhenString() throws Exception {
        // exercise
        final JsonValue value = underTest.parse("\"JSON string\"");

        // verify
        assertThat(value)
                .isJsonString()
                .hasType(TypeName.get(String.class))
                .hasValue("JSON string");
    }

    @Test
    public void parseShouldReturnJsonArrayWhenArray() throws Exception {
        // exercise
        final JsonValue value = underTest.parse("[]");

        // verify
        assertThat(value)
                .isJsonArray()
                .hasType(ParameterizedTypeName.get(List.class, Object.class))
                .hasValue(Collections.emptyList());
    }

    @Test
    public void parseShouldReturnJsonObjectWhenObject() throws Exception {
        // exercise
        final JsonValue value = underTest.parse("{}");

        // verify
        assertThat(value)
                .isJsonObject()
                .hasType(ParameterizedTypeName.get(Map.class, String.class, Object.class))
                .hasValue(Collections.emptyMap());
    }

    @Test
    public void parseShouldThrowExceptionWhenInvalidJson() throws Exception {
        // verify
        assertThatThrownBy(() -> {
            // exercise
            underTest.parse("{{invalid json}}");
        }).isInstanceOf(JsonParseException.class);
    }

    @Test
    public void parseShouldThrowExceptionWhenIOException() throws Exception {
        // setup
        doThrow(IOException.class)
                .when(mapper)
                .readValue(anyString(), eq(Object.class));

        // verify
        assertThatThrownBy(() -> {
            // exercise
            underTest.parse("{\"key\": \"value\"}");
        }).isInstanceOf(JsonParseException.class);
    }
}