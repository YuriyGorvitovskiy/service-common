package org.service.concept.db.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.concept.db.postgres.Execute.NEW_LINE;

import org.junit.Test;
import org.service.concept.db.event.RequestMerge;

import com.google.common.collect.ImmutableMap;

public class ExecuteMerge_UnitTest {

    @Test
    public void test_buildSql_simple() {
        // Setup
        ExecuteMerge subject = new ExecuteMerge();

        RequestMerge request = new RequestMerge("table",
                ImmutableMap.<String, Object>builder()
                    .put("id1", 123)
                    .put("id2", 234)
                    .build(),
                ImmutableMap.<String, Object>builder()
                    .put("col1", 12)
                    .put("col2", "Hello")
                    .put("col3", "World")
                    .build());

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("INSERT INTO table (id1, id2, col1, col2, col3)" + NEW_LINE +
                     "     VALUES (?, ?, ?, ?, ?)" + NEW_LINE +
                     "ON CONFLICT (id1, id2)" + NEW_LINE +
                     "  DO UPDATE" + NEW_LINE +
                     "        SET col1 = EXCLUDED.col1," + NEW_LINE +
                     "            col2 = EXCLUDED.col2," + NEW_LINE +
                     "            col3 = EXCLUDED.col3",
                result);

    }

}
