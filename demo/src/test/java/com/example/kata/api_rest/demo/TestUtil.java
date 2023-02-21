package com.example.kata.api_rest.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.time.OffsetDateTime;

public class TestUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String asJsonString(final Object obj) {
        try {

            ObjectMapper objectMapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static OffsetDateTime getOffsetDateTime(MockHttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        String dateTime = jsonNode.get("dateTime").asText();
        return OffsetDateTime.parse(dateTime);
    }
}
