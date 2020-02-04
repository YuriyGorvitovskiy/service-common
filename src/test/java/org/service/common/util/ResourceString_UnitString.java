package org.service.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ResourceString_UnitString {

    @Test
    public void toString_success() {
        // Setup
        ResourceString subject = new ResourceString(ResourceString_UnitString.class, "ResourceString_UnitString.filled.txt");

        // Execute
        String firstCall  = subject.toString();
        String secondCall = subject.toString();

        // Verify
        assertEquals("Hello Resource String!", firstCall);
        assertSame(firstCall, secondCall);
    }

    @Test
    public void toString_empty() {
        // Setup
        ResourceString subject = new ResourceString(ResourceString_UnitString.class, "ResourceString_UnitString.empty.txt");

        // Execute
        String result = subject.toString();

        // Verify
        assertEquals("", result);
    }

    @Test
    public void toString_failure() {
        // Setup
        ResourceString subject = new ResourceString(ResourceString_UnitString.class, "not-existing.txt");

        // Execute
        RuntimeException error = assertThrows(RuntimeException.class, () -> subject.toString());
        assertTrue(error.getMessage().contains("resource 'not-existing.txt' related to "
                + ResourceString_UnitString.class.getName()
                + " class"));
    }

}
