package org.service.command.dml.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.command.dml.postgres.DMLCommand.NEW_LINE;

import org.junit.Test;
import org.service.command.dml.UpsertParams;
import org.service.command.dml.postgres.Upsert;

import io.vavr.Tuple;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Stream;

public class ExecuteMerge_UnitTest {

    @Test
    public void test_buildSql_simple() {
        // Setup
        Upsert       subject = new Upsert();

        UpsertParams request = new UpsertParams("table",
                Stream.of(
                        Tuple.<String, Object>of("id1", 123),
                        Tuple.<String, Object>of("id2", 234))
                    .collect(LinkedHashMap.<String, Object>collector()),
                Stream.of(
                        Tuple.<String, Object>of("col1", 12),
                        Tuple.<String, Object>of("col2", "Hello"),
                        Tuple.<String, Object>of("col3", "World"))
                    .collect(LinkedHashMap.<String, Object>collector()));

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
