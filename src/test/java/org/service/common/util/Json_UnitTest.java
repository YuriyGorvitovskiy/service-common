package org.service.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class Json_UnitTest {

    @Test
    public void parseDate() {
        // Execute
        Date date = Json.parseDate("2018-10-12T19:39:13.123Z");

        // Verify
        assertEquals(new Date(1539373153123L).getTime(), date.getTime());
    }

    @Test
    public void parseDate_failed() {
        // Execute
        RuntimeException error = assertThrows(RuntimeException.class, () -> Json.parseDate("garbage"));
        assertTrue(error.getMessage().contains("garbage"));
    }

    @Test
    public void formatDate() {
        // Execute
        String str = Json.formatDate(new Date(1539373153123L));

        // Verify
        assertEquals("2018-10-12T19:39:13.123Z", str);
    }
}
