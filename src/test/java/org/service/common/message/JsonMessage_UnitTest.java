package org.service.common.message;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.service.common.test.Asserts;
import org.service.common.util.ResourceString;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMessage_UnitTest {

    public static class JsonTestMessage extends JsonMessage {
        public int field;
    }

    final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void read_message() throws Exception {
        // Setup
        final ResourceString jsonString = new ResourceString(JsonMessage_UnitTest.class,
                                                             "JsonMessage_UnitTest.serialization.message.json");

        // Execute
        JsonTestMessage message = mapper.readValue(jsonString.toString(), JsonTestMessage.class);

        // Validate JsonMessage
        assertEquals(0L, message.message_id);
        assertEquals(12, message.field);
    }

    @Test
    public void write_message() throws Exception {
        // Setup
        ResourceString resource = new ResourceString(JsonMessage_UnitTest.class,
                                                     "JsonMessage_UnitTest.serialization.message.json");

        // Execute & Validate
        Asserts.asserJsonRoundtrip(mapper, resource.toString(), JsonTestMessage.class);
    }
}
