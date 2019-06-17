package org.service.command.dml.postgres;

import static org.junit.Assert.assertEquals;
import static org.service.command.dml.postgres.DMLCommand.NEW_LINE;

import org.junit.Test;
import org.service.command.dml.InsertParams;
import org.service.command.dml.postgres.Insert;

import io.vavr.Tuple;
import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Stream;

public class Insert_UnitTest {

    @Test

    public void test_buildSql_simple() {
        // Setup
        Insert       subject = new Insert();

        InsertParams request = new InsertParams("table",
                Stream.of(
                        Tuple.<String, Object>of("col1", 12),
                        Tuple.<String, Object>of("col2", "Hello"),
                        Tuple.<String, Object>of("col3", "World"))
                    .collect(LinkedHashMap.<String, Object>collector()));

        // Execute
        String result = subject.buildSql(request);

        // Verify
        assertEquals("INSERT INTO table (col1, col2, col3)" + NEW_LINE +
                "VALUES (?, ?, ?)",
                result);

    }

}
