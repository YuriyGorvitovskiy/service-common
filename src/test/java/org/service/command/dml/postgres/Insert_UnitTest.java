package org.service.concept.db.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.concept.db.postgres.Execute.NEW_LINE;

import org.junit.Test;
import org.service.concept.db.event.RequestInsert;

import com.google.common.collect.ImmutableMap;

public class ExecuteInsert_UnitTest {

    @Test
    public void test_buildSql_simple() {
        // Setup
        ExecuteInsert subject = new ExecuteInsert();

        RequestInsert request = new RequestInsert("table",
                ImmutableMap.<String, Object>builder()
                    .put("col1", 12)
                    .put("col2", "Hello")
                    .put("col3", "World")
                    .build());

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("INSERT INTO table (col1, col2, col3)" + NEW_LINE +
                     "VALUES (?, ?, ?)",
                result);

    }

}
